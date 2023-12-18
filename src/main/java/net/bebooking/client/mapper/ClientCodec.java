package net.bebooking.client.mapper;

import com.mongodb.MongoClientSettings;
import net.bebooking.client.model.Client;
import net.bebooking.client.model.ClientId;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.Optional;

public class ClientCodec implements CollectibleCodec<Client> {

    private final CodecRegistry codecRegistry;
    private final Codec<Document> documentCodec;
    private final ClientConverter converter;

    public ClientCodec() {
        this.codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        this.documentCodec = this.codecRegistry.get(Document.class);
        this.converter = new ClientConverter();
    }

    public ClientCodec(Codec<Document> codec) {
        this.codecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        this.documentCodec = codec;
        this.converter = new ClientConverter();
    }

    public ClientCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
        this.documentCodec = this.codecRegistry.get(Document.class);
        this.converter = new ClientConverter();
    }

    @Override
    public Client generateIdIfAbsentFromDocument(Client value) {
        return documentHasId(value) ? value
                : Client.of(
                ClientId.generate(),
                value.getFullName(),
                value.getEmail(),
                value.getPhoneNumber(),
                value.getDescription(),
                value.getCountry(),
                value.getCity(),
                value.getCreatedAt()
        );
    }

    @Override
    public boolean documentHasId(Client value) {
        return value.getId().isEmpty();
    }

    @Override
    public BsonValue getDocumentId(Client value) {
        return Optional.of(new BsonString(value.getId().getValue().toString()))
                .orElseThrow(() -> new NullPointerException("ClientId is null"));
    }

    @Override
    public Client decode(BsonReader reader, DecoderContext decoderContext) {
        var doc = documentCodec.decode(reader, decoderContext);
        return this.converter.convert(doc);
    }

    @Override
    public void encode(BsonWriter writer, Client value, EncoderContext encoderContext) {
        var doc = this.converter.convert(value);
        documentCodec.encode(writer, doc, encoderContext);
    }

    @Override
    public Class<Client> getEncoderClass() {
        return Client.class;
    }
}
