package io.dataease.filter;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class HtmlResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream contentBuffer = new ByteArrayOutputStream();
    private final PrintWriter writer = new PrintWriter(contentBuffer);

    public HtmlResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStreamWrapper(contentBuffer);
    }

    public byte[] getContent() {
        writer.flush();
        return contentBuffer.toByteArray();
    }

    private static class ServletOutputStreamWrapper extends ServletOutputStream {
        private final ByteArrayOutputStream buffer;

        public ServletOutputStreamWrapper(ByteArrayOutputStream buffer) {
            this.buffer = buffer;
        }

        @Override
        public void write(int b) {
            buffer.write(b);
        }

        @Override
        public boolean isReady() {
            // 返回 true，表示准备好写入数据
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // 这是一个简单的实现，你可以根据需要进行更复杂的处理
            throw new UnsupportedOperationException("Not implemented");
        }
    }
}
