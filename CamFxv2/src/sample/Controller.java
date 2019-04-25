package sample;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamResolution;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmediaimpl.platform.Platform;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.ScheduledExecutorService;

class TakePhoto implements Callable<Image> {
    private Webcam webcam;
    private File imageFile;
    private BufferedImage image;
    private Image takenImage;
    public TakePhoto(Webcam cam,File imgfile){
        this.webcam = cam;
        this.imageFile = imgfile;
    }
        public Image call() throws Exception{
        if(image!=null) image.flush();
            webcam.open();
            image = webcam.getImage();
            ImageIO.write(image, "PNG", imageFile);
            takenImage = new Image("file:" + imageFile.toPath());
            Thread.sleep(1000);
            return takenImage;
        }
}

public class Controller {
    private Webcam webcam;
    private BufferedImage getImage;
    private File imagefile;
    private Image takenImage;
    public Controller() {
        webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        imagefile = new File("photo.png");
        //Queue<Image>images = new ArrayDeque<Image>();
        System.out.println("task start");
        webcam.open();
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(50), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //System.out.println("Execute!!");
                if(getImage!=null) getImage.flush();
                getImage = webcam.getImage();
                /*
                写真を取得して書き込みする
                try {
                    ImageIO.write(getImage, "PNG", imagefile);
                }catch (Exception e){
                    System.out.println("Error!!!");
                }*/
                /*
                写真を取得して書き込みする
                Fimage.setImage(new Image("file:" + imagefile.toPath()));
                 */
                /*写真を書き込まないで表示する*/
                Fimage.setImage(SwingFXUtils.toFXImage(getImage,null));
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

    }

    @FXML
    ImageView Fimage = null;

    @FXML
    protected void takePhoto(ActionEvent event) throws Exception {
        ExecutorService service = Executors.newSingleThreadExecutor();
        System.out.println("task start");
        /*while (true) {
            Future<Image> future = service.submit(new TakePhoto(webcam, imagefile));
            takenImage = future.get();
            Fimage.setImage(takenImage);
            //service.shutdown();
        }*/
    }

    @FXML
    protected void cycleTake() throws Exception{
       /* System.out.println("task start");
        Timeline timer = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Execute!!");
                if(getImage!=null) getImage.flush();
                webcam.open();
                getImage = webcam.getImage();
                try {
                    ImageIO.write(getImage, "PNG", imagefile);
                }catch (Exception e){
                    System.out.println("Error!!!");
                }
                takenImage = new Image("file:" + imagefile.toPath());
                Fimage.setImage(takenImage);
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();*/
    }
}