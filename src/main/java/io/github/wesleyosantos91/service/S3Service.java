package io.github.wesleyosantos91.service;

import io.github.wesleyosantos91.domain.PersonDomain;
import io.github.wesleyosantos91.schema.PersonSchema;
import io.github.wesleyosantos91.request.Person;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.conf.Configuration;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.io.LocalOutputFile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static java.time.LocalDate.of;

@Service
public class S3Service {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssSSS");
    private static final DateTimeFormatter FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmssSSS");
    public static final int PAGE_SIZE = 256 * 1024;
    public static final long ROW_GROUP_SIZE = 128 * 1024 * 1024L;

    private final Configuration configuration;
    private final S3Client s3Client;
    private final PersonSchema personSchema;

    public S3Service(S3Client s3Client, Configuration configuration, PersonSchema personSchema) {
        this.s3Client = s3Client;
        this.configuration = configuration;
        this.personSchema = personSchema;
    }

    public void upload(Person person, String bucketName) throws IOException {

        final PersonDomain personDomain = new PersonDomain();
        personDomain.setId(UUID.randomUUID().toString());
        personDomain.setNome(person.name());
        personDomain.setDataNascimento(of(1991, 6,12).toString());
        personDomain.setCodigoInterno(33);
        personDomain.setDataHoraCriacaoRegistro(LocalDateTime.now().format(DATE_TIME_FORMATTER));

        final LocalOutputFile localOutputFile = new LocalOutputFile(Paths.get(UUID.randomUUID() + ".parquet"));

        try (final ParquetWriter<GenericRecord> writer = AvroParquetWriter
                .<GenericRecord>builder(localOutputFile)
                .withConf(configuration)
                .withSchema(personSchema.getSchema())
                .withCompressionCodec(CompressionCodecName.ZSTD) // Compressão para economizar espaço
                .withPageSize(PAGE_SIZE) // Tamanho de página otimizado para reduzir overhead de memória
                .withRowGroupSize(ROW_GROUP_SIZE) // Tamanho de grupo de linhas para melhor desempenho de leitura/gravação
                .withDictionaryEncoding(true) // Habilita o dicionário de página
                .build()) {

            //final GenericRecord record = personSchema.toAvro(personDomain);
            final List<GenericRecord> records = personSchema.toAvroList(List.of(personDomain, personDomain, personDomain));
            for (GenericRecord record : records) {
                writer.write(record);
            }
        }

        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            outputStream.write(Files.readAllBytes(Paths.get(localOutputFile.getPath())));
            final String fileName = "eventos-" + LocalDateTime.now().format(FILE_NAME_FORMATTER) + ".parquet";

            final PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            s3Client.putObject(request, RequestBody.fromBytes(outputStream.toByteArray()));
        }


        // Deletar o arquivo temporário
        Files.delete(Paths.get(localOutputFile.getPath()));
    }
}
