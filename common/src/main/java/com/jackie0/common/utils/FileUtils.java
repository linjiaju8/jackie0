package com.jackie0.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 文件操作工具类
 */
public class FileUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

    private static final int BUFFER_SIZE = 1024 * 200; // 20M

    private FileUtils() {
    }

    public static void newIODownload(ServletOutputStream servletOutputStream, String filePathAndFileName) throws IOException {
        long startTime = System.currentTimeMillis();
        try (FileChannel fileChannelIn = new RandomAccessFile(filePathAndFileName, "r").getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE); // 从日志文件每次写入的缓存
            while (fileChannelIn.read(byteBuffer) != -1) {
                byteBuffer.flip();
                servletOutputStream.write(byteBuffer.array());
                byteBuffer.clear();
            }
        } catch (IOException ie) {
            LOGGER.error("通过NIO方式下载文件异常：", ie);
            throw ie;
        } finally {
            LOGGER.debug("NIO文件下载执行时间-->{}秒", (System.currentTimeMillis() - startTime) / 1000);
        }
    }
}
