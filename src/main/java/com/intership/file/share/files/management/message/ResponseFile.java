package com.intership.file.share.files.management.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseFile {
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;


}



