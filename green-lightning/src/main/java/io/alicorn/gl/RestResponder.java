package io.alicorn.gl;

import com.ociweb.gl.api.GreenCommandChannel;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.api.PubSubListener;
import com.ociweb.gl.api.Writable;
import com.ociweb.json.encode.JSONRenderer;
import com.ociweb.pronghorn.network.config.HTTPContentTypeDefaults;
import com.ociweb.pronghorn.pipe.ChannelReader;
import com.ociweb.pronghorn.pipe.ChannelWriter;

public class RestResponder implements PubSubListener{

    private final GreenCommandChannel cmd;

    private static final JSONRenderer<ChannelReader> jsonRenderer = new JSONRenderer<ChannelReader>()
            .beginObject()
            .string("name", o->o.readUTF())
            .bool("happy", o->o.readBoolean())
            .integer("age", o->o.readPackedInt())
            .endObject();

    private ChannelReader payloadW;
    private Writable w = new Writable() {
        @Override
        public void write(ChannelWriter writer) {
            jsonRenderer.render(writer, payloadW);
        }
    };

    public RestResponder(GreenRuntime runtime) {
        cmd = runtime.newCommandChannel();
        cmd.ensureHTTPServerResponse(128,400);
    }

    @Override
    public boolean message(CharSequence topic, ChannelReader payload) {

        payloadW = payload;
        return cmd.publishHTTPResponse(
                payload.readPackedLong(),
                payload.readPackedLong(),
                200, false, HTTPContentTypeDefaults.JSON, w);
    }

}