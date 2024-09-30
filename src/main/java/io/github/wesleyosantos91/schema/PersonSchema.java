package io.github.wesleyosantos91.schema;

import io.github.wesleyosantos91.domain.PersonDomain;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PersonSchema {

    private final Schema SCHEMA = SchemaBuilder.record("Person")
            .fields()
            .name("id").type().stringType().noDefault()
            .name("nome").type().stringType().noDefault()
            .name("codigoInterno").type().intType().noDefault()
            .name("dataNascimento").type().stringType().noDefault()
            .name("dataHoraCriacaoRegistro").type().stringType().noDefault()
            .endRecord();

    public Schema getSchema() {
        return SCHEMA;
    }

    public GenericRecord toAvro(PersonDomain personDomain) {
        GenericRecord record = new GenericData.Record(getSchema());
        record.put("id", personDomain.getId());
        record.put("nome", personDomain.getNome());
        record.put("codigoInterno", personDomain.getCodigoInterno());
        record.put("dataNascimento", personDomain.getDataNascimento());
        record.put("dataHoraCriacaoRegistro", personDomain.getDataHoraCriacaoRegistro());
        return record;
    }

    public List<GenericRecord> toAvroList(List<PersonDomain> personDomains) {
        return personDomains.stream()
                .map(this::toAvro)
                .collect(Collectors.toList());
    }
}
