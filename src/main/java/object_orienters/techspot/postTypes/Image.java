package object_orienters.techspot.postTypes;

import java.sql.Blob;

public class Image extends DataType {

    private Blob image;

    public Blob getImage() {
        return image;
    }

    public void setImage(Blob image) {
        this.image = image;
    }
}
