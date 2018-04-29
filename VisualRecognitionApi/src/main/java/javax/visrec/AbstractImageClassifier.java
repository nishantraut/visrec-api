package javax.visrec;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;
import javax.visrec.internal.ImageFactoryProvider;
import javax.visrec.ml.classification.Classifier;
import javax.visrec.util.Builder;
import javax.visrec.util.ImageFactory;

/**
 * Skeleton abstract class to make it easier to implement image classifier.
 *
 * @param <IMAGE_CLASS>
 * @param <MODEL_CLASS>
 * @author Zoran Sevarac <zoran.sevarac@deepnetts.com>
 */
public abstract class AbstractImageClassifier<IMAGE_CLASS, MODEL_CLASS> implements Classifier<IMAGE_CLASS>, Builder<Classifier> { // could also implement binary classifier

    private ImageFactory<IMAGE_CLASS> imageFactory; // get image factory for the specified image class
    private MODEL_CLASS model; // th emodel could be injected from machine learning container

    private float threshold;

    protected AbstractImageClassifier(final Class<IMAGE_CLASS> cls) {
        final Optional<ImageFactory<IMAGE_CLASS>> optionalImageFactory = ImageFactoryProvider.getInstance().findImageFactory(cls);
        if (!optionalImageFactory.isPresent()) {
            throw new IllegalArgumentException(String.format("Could not find ImageFactory by '%s'", cls.getName()));
        }
        imageFactory = optionalImageFactory.get();
    }

    public ImageFactory<IMAGE_CLASS> getImageFactory() {
        return imageFactory;
    }

    public Map<String, Float> classify(File file) throws IOException {
        IMAGE_CLASS image = imageFactory.getImage(file);
        return classify(image);
    }

//    public ClassificationResults<BoundingBox> classify(URL url) throws IOException {
//        IMAGE_CLASS image = imageFactory.getImage(url);
//         return classify(image);
//    }
    public Map<String, Float> classify(InputStream inStream) throws IOException {
        IMAGE_CLASS image = imageFactory.getImage(inStream);
        return classify(image);
    }

    public void setImageFactory(ImageFactory<IMAGE_CLASS> imageFactory) {
        this.imageFactory = imageFactory;
    }

    public MODEL_CLASS getModel() {
        return model;
    }

    protected void setModel(MODEL_CLASS model) {
        this.model = model;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setThreshold(float threshold) {
        this.threshold = threshold;
    }
}
