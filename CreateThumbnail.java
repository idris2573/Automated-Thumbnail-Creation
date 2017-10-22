import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;

/**
 * Created by Idris on 26/08/2017.
 */
public class CreateThumbnail extends Application {

    public static void main(String [] args){
        launch(args);
    }

    Pane mainPane;

    ImageView pokemonImage;
    Label pokemonName;
    ImageView backgroundImage;
    Stage window;

    String img;
    String fileName;
    String name;

    int bgNo;
    int length;
    int count;

    ReadVideoInfo readVideoInfo = new ReadVideoInfo();

    public void start(Stage primaryStage) throws Exception {

        readVideoInfo.read("files/pokemonInfo.csv");
        count = 0;
        length = readVideoInfo.pokemonNo.size();

        while(count < length) {

            try {
                fileName = readVideoInfo.pokemonNo.get(count);
                img = readVideoInfo.pokemonNo.get(count) + ".png";
                name = readVideoInfo.name.get(count);

                if (!new File("files/youtubeImgs/" + fileName + ".png").exists() && !img.contains("2b")) {

                   if(img.contains("jpg") || img.contains("png")) {
                       //System.out.println("img : " + img);
                        window = primaryStage;
                        window.setTitle("Thumbnail Image");

                        mainPane = new Pane();
                        mainPane.setStyle("-fx-background-color: white;");

                        setBgImage();
                        setPokemonImage();


                        //mainPane.getChildren().add(productImage);
                        mainPane.getChildren().add(backgroundImage);
                        mainPane.getChildren().add(pokemonImage);
                        mainPane.getChildren().add(pokemonName);

                        setScene(false);
                        render();
                        //Thread.sleep(1000);
                    }
                }

            }catch (Exception e){
                System.out.println("Failed...");
            }
            count++;
        }

        System.out.println("Complete");
        System.exit(0);
        Platform.exit();
    }


    void setBgImage(){
        bgNo = (int)(Math.random() * 8 + 1);

        Image image = new Image("file:" + "files/bg/thumbnail.png");
        backgroundImage = new ImageView(image);
        //backgroundImage.setFitHeight(735);

    }

    void setPokemonImage(){

        Image image = new Image("file:" + "files/pokemonImages/" + img);
        pokemonImage = new ImageView(image);
        pokemonImage.setPreserveRatio(true);
        pokemonImage.setFitWidth(650);
        pokemonImage.setTranslateX(580);
        pokemonImage.setTranslateY(50);

        pokemonName = new Label(name.toUpperCase());
        pokemonName.setFont(new Font(100));
        pokemonName.setTranslateX(0);
        pokemonName.setPadding(new Insets(0,60,0,30));
        pokemonName.setTranslateY(500);
        pokemonName.setStyle("-fx-background-color: #465ffa; -fx-font-weight: bold; -fx-text-fill: white");

    }

    void setScene(boolean show){
        Scene scene = new Scene(mainPane, 1280 ,720); // size of the window / scene
        window.setScene(scene);
        window.setResizable(false);

        if(show){
            window.show(); //show window
        }
    }

    public void render() {

        try {

            String printFilename = fileName;

            if (printFilename.length() > 50){
                printFilename = printFilename.substring(0,50) + "...";
            }


                int percentage = (count * 100) / length;
                System.out.println("Rendering " + count + "/" + length + "  " + percentage + "% " + img + " " + printFilename);


                WritableImage image = mainPane.snapshot(new SnapshotParameters(), null);



                new File("files/youtubeImgs/").mkdirs();
                File file = new File("files/youtubeImgs/" + fileName + ".png");

                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);



        }catch(Exception e){
            System.out.println("Could not render image");
        }

        if (count >= length) {
            System.out.println("Render complete");
            window.close();
        }

    }
}
