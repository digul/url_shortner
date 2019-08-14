package cf.digul.shortener.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class RequestWrapper extends HttpServletRequestWrapper {
	
	private byte[] rawData;
    private ByteArrayInputStream bis;
    private final String body;

	public RequestWrapper(HttpServletRequest request) throws IOException {
		super(request);

        this.rawData = IOUtils.toByteArray(super.getInputStream());
        this.bis = new ByteArrayInputStream(rawData);
        this.body = new String(rawData);
	}
	
	@Override
	public ServletInputStream getInputStream() {
        return new ServletInputStream() {
            @Override
            public boolean isFinished() {
                return bis.available() == 0;
            }
 
            @Override
            public boolean isReady() {
                return true;
            }
 
            @Override
            public int read() {
                return bis.read();
            }

			@Override
			public void setReadListener(ReadListener listener) {
				return;
			}
        };
    }
	
	public String getBody() {
		return this.body;
	}
}
