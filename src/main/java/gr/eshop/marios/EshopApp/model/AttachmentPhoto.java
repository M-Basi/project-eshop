package gr.eshop.marios.EshopApp.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name= "attachmentPhotos")
public class AttachmentPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;
    private String savedName;
    private String filePath;
    private String contentType;
    private String extension;


}
