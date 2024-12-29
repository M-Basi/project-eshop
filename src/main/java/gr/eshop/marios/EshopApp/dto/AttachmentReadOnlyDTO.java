package gr.eshop.marios.EshopApp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AttachmentReadOnlyDTO {
    private String filename;
    private String savedName;
    private String filePath;
    private String contentType;
    private String extension;
}
