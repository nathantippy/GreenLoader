package io.alicorn.gl;

import com.ociweb.gl.api.GreenCommandChannel;
import com.ociweb.gl.api.GreenRuntime;
import com.ociweb.gl.api.HTTPRequestReader;
import com.ociweb.gl.api.MsgCommandChannel;
import com.ociweb.gl.api.RestListener;
import com.ociweb.gl.api.Writable;
import com.ociweb.pronghorn.pipe.ChannelWriter;

public class RestConsumer implements RestListener {

    private GreenCommandChannel cmd2;
    private HTTPRequestReader requestW;
    private final long fieldA;
    private final long fieldB;
    private final long fieldC;

    private Writable w = new Writable() {

        @Override
        public void write(ChannelWriter writer) {
            // Write connection data.
            writer.writePackedLong(requestW.getConnectionId());
            writer.writePackedLong(requestW.getSequenceCode());

            // Write JSON data.
//            writer.writeUTF8Text(requestW.structured().readText(fieldA));
            writer.writeBoolean(requestW.structured().readBoolean(fieldB));
            writer.writeInt(requestW.structured().readInt(fieldC));
        }

    };
    public RestConsumer(GreenRuntime runtime, long fieldA, long fieldB, long fieldC) {
        this.cmd2 = runtime.newCommandChannel();
        this.cmd2.ensureDynamicMessaging();
        this.cmd2.ensureHTTPServerResponse();
        this.fieldA = fieldA;
        this.fieldB = fieldB;
        this.fieldC = fieldC;
    }


    @Override
    public boolean restRequest(final HTTPRequestReader request) {

        if (!( request.isVerbPost() || request.isVerbGet() )) {
            cmd2.publishHTTPResponse(request, 404);
        }

        requestW = request;
        return cmd2.publishTopic("/send/200", w);
    }
}