package gr.eshop.marios.EshopApp.repository;

import gr.eshop.marios.EshopApp.model.AttachmentPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttachmentPhotoRepository extends JpaRepository<AttachmentPhoto, Long>, JpaSpecificationExecutor<AttachmentPhoto> {


}
