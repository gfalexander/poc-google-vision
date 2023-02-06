import com.google.cloud.vision.v1.*;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GvisionApplication {

    public static void labelAnnotation(String[] args) throws Exception{
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {

            String filename = "C:\\Users\\Consultor\\Documents\\Matrix\\poc-gvision\\src\\main\\resources\\img\\Copel-Luz-marco-2020.pdf_page0.jpg";

            Path path = Paths.get(filename);
            byte[] data = Files.readAllBytes(path);
            ByteString imgBytes = ByteString.copyFrom(data);

            List<AnnotateImageRequest> requests = new ArrayList<>();
            Image img = Image.newBuilder().setContent(imgBytes).build();
            Feature feat = Feature.newBuilder().setType(Type.LABEL_DETECTION).build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
            requests.add(request);

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation
                            .getAllFields()
                            .forEach((k, v) -> System.out.format("%s : %s%n", k, v.toString()));
                }
            }

        }
    }

    public static void detectTextInImage(String imgPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(imgPath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
        AnnotateImageRequest resquest = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

        requests.add(resquest);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            List<String> teste = new ArrayList<>();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    return;
                }

                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {

                    if (annotation.getDescription().contains("FASICO")) {
                        teste.add(annotation.getDescription());
                    }
//                    System.out.format("Text: %s%n", annotation.getDescription());
//                    System.out.format("Position : %s%n", annotation.getBoundingPoly());
                }
            }
            System.out.println(teste);
        }
    }

    public static void main(String[] args) throws IOException {
        String filePath = "C:\\Users\\Consultor\\Documents\\Matrix\\poc-gvision\\src\\main\\resources\\img\\com_flash.jpg";
        detectTextInImage(filePath);
    }
}
