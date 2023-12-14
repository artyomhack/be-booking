package net.bebooking.booking.mapper;

import com.mongodb.MongoClient;
import net.bebooking.booking.model.Booking;
import org.bson.BsonReader;
import org.bson.BsonValue;
import org.bson.BsonWriter;
import org.bson.Document;
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
    public Booking generateIdIfAbsentFromDocument(Booking document) {
        return null;
    }

    @Override
    public boolean documentHasId(Booking document) {
        return false;
    }

    @Override
    public BsonValue getDocumentId(Booking document) {
        return null;
    }

    @Override
    public Booking decode(BsonReader reader, DecoderContext decoderContext) {
        return null;
    }

    @Override
    public void encode(BsonWriter writer, Booking value, EncoderContext encoderContext) {

    }

    @Override
    public Class<Booking> getEncoderClass() {
        return null;
    }
}
