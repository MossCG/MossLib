package org.mossmc.mosscg.MossLib.Info;

import oshi.hardware.NetworkIF;

import java.util.concurrent.TimeUnit;

public class InfoNetwork {
    /**
     * 网络基础信息
     */
    public static void updateNetworkUse() throws Exception{
        long bytesReceiveStart = 0;
        long bytesSendStart = 0;
        for (NetworkIF net : InfoManager.hardware.getNetworkIFs()) {
            bytesReceiveStart = bytesReceiveStart + net.getBytesRecv();
            bytesSendStart = bytesSendStart + net.getBytesSent();
        }
        TimeUnit.SECONDS.sleep(1);
        long bytesReceiveEnd = 0;
        long bytesSendEnd = 0;
        for (NetworkIF net : InfoManager.hardware.getNetworkIFs()) {
            bytesReceiveEnd = bytesReceiveEnd + net.getBytesRecv();
            bytesSendEnd = bytesSendEnd + net.getBytesSent();
        }
        uploadBand = (bytesSendEnd-bytesSendStart)/128000.0;
        downloadBand = (bytesReceiveEnd-bytesReceiveStart)/128000.0;
        uploadTotal = bytesSendEnd/1073741824.0;
        downloadTotal = bytesReceiveEnd/1073741824.0;
    }

    private static double uploadBand = 0.00;
    private static double downloadBand = 0.00;
    private static double uploadTotal = 0.00;
    private static double downloadTotal = 0.00;

    /**
     * 获取网络占用
     * 注意，本方法不会返回即时数值，有可能是上个10s的，请求一次后间隔1.5s左右再次请求最准确
     * band返回带宽，单位Mbps
     * total返回流量，单位GB
     */
    public static double getUploadBand() {
        InfoManager.checkUpdate();
        return uploadBand;
    }
    public static double getDownloadBand() {
        InfoManager.checkUpdate();
        return downloadBand;
    }
    public static double getUploadTotal() {
        InfoManager.checkUpdate();
        return uploadTotal;
    }
    public static double getDownloadTotal() {
        InfoManager.checkUpdate();
        return downloadTotal;
    }
}
