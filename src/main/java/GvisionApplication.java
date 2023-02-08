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

    public static List<String> detectTextInImage(String imgPath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(imgPath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.TEXT_DETECTION).build();
        AnnotateImageRequest resquest = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();

        requests.add(resquest);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            List<String> content = new ArrayList<>();;
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                }
                List<EntityAnnotation> annotations = res.getTextAnnotationsList();

                for (int i = 0; i < annotations.size(); i++) {
                    if (i > 0) {
                        content.add(annotations.get(i).getDescription());
                    }
                }
//                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//
//                    if (annotation.getDescription().contains("FASICO")) {
//                        teste.add(annotation.getDescription());
//                    }
////                    System.out.format("Text: %s%n", annotation.getDescription());
////                    System.out.format("Position : %s%n", annotation.getBoundingPoly());
//                }
            }
            return content;
        }
    }

    public static void filterMonthInfo(List<String> contents) {
        List<String> monthInfo = new ArrayList<>();
        boolean flagMonth = false;
        int indexMonth = 12;

        for (String content : contents) {
            if (flagMonth && indexMonth > 0) {
                monthInfo.add(content);
                indexMonth--;
            }
            if (content.equals("Més") || content.equals("Mês") || content.equals("Mes")) {
                flagMonth = true;
                monthInfo.add(content);
            }
        }

        System.out.println(monthInfo);
    }

    public static void filterKwh(List<String> contents) {
        List<String> measuresKwh = new ArrayList<>();
        boolean flagKwh = false;
        int indexKwh = 12;
        int tryIndex = 3;

        for (String content : contents) {
            if (flagKwh && indexKwh > 0) {
                if (tryIndex > 0) {
                    try {
                        if(content.contains("ооо")) {
                            List<String> contentToZero = List.of(content.split(""));

                            for (int i = 0; i < contentToZero.size(); i++) {
                                measuresKwh.add("0");
                                indexKwh--;
                            }
                        } else {
                            Integer.valueOf(content);
                            measuresKwh.add(content);
                            indexKwh--;
                        }
                    } catch (Exception e) {
                        tryIndex--;
                        if (tryIndex == 0) {
                            tryIndex = 3;
                            flagKwh = false;
                            measuresKwh.remove(0);
                        }
                    }
                }
            }
            if (content.equals("kWh")) {
                if (measuresKwh.isEmpty()) {
                    measuresKwh.add(content);
                }
                flagKwh = true;
            }
        }
            System.out.println(measuresKwh);
    }

    public static void filterMeasurer(List<String> contents) {
        List<String> measurers = new ArrayList<>();

        for (String content : contents) {
            if (content.contains("FASICO")) {
                measurers.add(content);
            }
        }

        System.out.println(measurers);
    }

    public static void filterTariffs(List<String> contents) {
        List<String> tariffs = new ArrayList<>();
        boolean flagTariffs = false;
        int tryIndex = 2;

        for (String content : contents) {
            if (flagTariffs) {
                try {
                    content = content.replace(",", ".");

                    Double.valueOf(content);
                    tariffs.add(content);
                    flagTariffs = false;

                } catch (Exception e) {
                    tryIndex--;
                }
            }

            if (content.equals("Tarifas")) {
                tariffs.add(content);
                flagTariffs = true;
            }
        }
        System.out.println(tariffs);

    }

    public static void main(String[] args) throws IOException {
        String tmpPath = System.getProperty("java.io.tmpdir");
        String filePath = String.format("%s/com_flash.jpg", tmpPath);
        List<String> contents = detectTextInImage(filePath);

        filterMonthInfo(contents);
        filterMeasurer(contents);
        filterKwh(contents);
        filterTariffs(contents);
    }
}
