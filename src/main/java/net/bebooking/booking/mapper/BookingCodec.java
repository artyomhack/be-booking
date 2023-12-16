package net.bebooking.booking.mapper;

import com.mongodb.MongoClient;
import net.bebooking.booking.model.Booking;
import net.bebooking.booking.model.BookingId;
import org.bson.*;
import org.bson.codecs.Codec;
import org.bson.codecs.CollectibleCodec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

public class BookingCodec implements CollectibleCodec<Booking> {

    /**
     * registry - Реестр кодеков, определяет, как преобразовать java to Bson
     *
     * documentCodec - Интерфейс, определяющий методы для сериализации
     * и десериализации объектов между Java и BSON.
     *
     * converter - Передаёт данные из mongo в java объект, некий mapper
     */
    private final CodecRegistry registry;
    private final Codec<Document> documentCodec;
    private final BookingConverter converter;

    public BookingCodec() {
        this.registry = MongoClient.getDefaultCodecRegistry();
        this.documentCodec = this.registry.get(Document.class);
        this.converter = new BookingConverter();
    }

    public BookingCodec(Codec<Document> codec) {
        this.registry = MongoClient.getDefaultCodecRegistry();
        this.documentCodec = codec;
        this.converter = new BookingConverter();
    }

    public BookingCodec(CodecRegistry codecRegistry) {
        this.registry = codecRegistry;
        this.documentCodec = this.registry.get(Document.class);
        this.converter = new BookingConverter();
    }

    @Override
    public Booking generateIdIfAbsentFromDocument(Booking value) {
        return documentHasId(value) ? value
                : Booking.of(
                BookingId.generate(),
                value.getFrom(),
                value.getTo(),
                value.getStatus(),
                value.getNote(),
                value.getCreatedAt()
        );
    }

    @Override
    public boolean documentHasId(Booking value) {
        return value.getId().isEmpty();
    }

    @Override
    public BsonValue getDocumentId(Booking value) {
        if (!documentHasId(value))
            throw new IllegalArgumentException("The document does not contain an _id");
        return new BsonString(value.getId().getValue().toString());
    }

    @Override
    public Booking decode(BsonReader reader, DecoderContext decoderContext) {
        var doc = documentCodec.decode(reader, decoderContext);
        return this.converter.convert(doc);
    }

    @Override
    public void encode(BsonWriter writer, Booking value, EncoderContext encoderContext) {
        var doc = this.converter.convert(value);
        documentCodec.encode(writer,doc, encoderContext);
    }

    @Override
    public Class<Booking> getEncoderClass() {
        return Booking.class;
    }
}
