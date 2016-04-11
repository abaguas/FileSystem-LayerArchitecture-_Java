package pt.tecnico.mydrive.service;

import pt.tecnico.mydrive.domain.MyDrive;

public class DeleteFileService extends MyDriveService {
    private String fileName;
    private String content;
    private long token;

    public DeleteFileService(String fileName, long token) {
    }
    
    public final void dispatch() {
            
    }
}
