package ru.tehkode.mualauncher.net;

/**
 *
 * @author t3hk0d3
 */
public interface DownloadListener {
    
    public void onDownloadProgress(long readed, long total);
    
    public void onDownloadStarted(long total);
    
    public void onDownloadFinished(long readed);
    
}
