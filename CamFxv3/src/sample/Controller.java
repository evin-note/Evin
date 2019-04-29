package sample;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamDiscoveryListener;
import com.github.sarxos.webcam.WebcamResolution;
import com.sun.javafx.tk.Toolkit;
import com.sun.media.jfxmediaimpl.platform.Platform;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;
import com.xuggle.xuggler.IPixelFormat;
import com.xuggle.xuggler.IVideoPicture;
import com.xuggle.xuggler.video.ConverterFactory;
import com.xuggle.xuggler.video.IConverter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.SimpleFormatter;


public class Controller {
    private Webcam webcam;
    private BufferedImage getImage;
    private File imagefile;
    private File moviefile;
    private boolean photo = false;
    private boolean movie = false;
    private Dimension camerasize = null;
    @FXML
    ImageView Fimage = null;
    @FXML
    Button startButton;
    @FXML
    Button endButton;
    @FXML
    Button movieButton;

    public Controller() {
        imagefile = new File("photo.png");
        moviefile = new File("output.mp4");
        System.out.println(moviefile.getName());
    }

    @FXML
    protected void startStream(ActionEvent event){
        try {
            webcam = Webcam.getDefault();
            camerasize = WebcamResolution.VGA.getSize();
            webcam.setViewSize(camerasize);
            System.out.println(webcam.getName());
        }catch (Exception e){
            System.out.println("Not found");
        }
        photo = true;
        startButton.setDisable(true);
        endButton.setDisable(false);
        movieButton.setDisable(false);
        Task<Void> taskPhoto = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("task start");
                webcam.open();
                while (photo) {
                    try {
                        getImage = webcam.getImage();
                        System.out.println("taken");
                    }catch (Exception e){
                        System.out.println("can't take");
                    }
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
                    Fimage.setImage(SwingFXUtils.toFXImage(getImage, null));
                    getImage.flush();
                }
                return null;
            }
        };
        Thread thpht = new Thread(taskPhoto);
        //thpht.setDaemon(true);
        thpht.start();
    }

    @FXML
    protected void endStream(ActionEvent event){
        startButton.setDisable(false);
        endButton.setDisable(true);
        movieButton.setDisable(true);
        photo = false;
    }

    @FXML
    protected void startMovie(ActionEvent event){
        if (movie){
            movie = false;
            startButton.setDisable(false);
            endButton.setDisable(false);
        } else {
            movie = true;
            startButton.setDisable(true);
            endButton.setDisable(true);
        }
        long start = System.currentTimeMillis();;
        boolean pref = false;
        //init
        String filename = "output.mp4";
        IMediaWriter writer = ToolFactory.makeWriter(moviefile.getName());
        writer.addAudioStream(0,0, ICodec.ID.CODEC_ID_H264,camerasize.width,camerasize.height);
        //init
        Task<Void> taskMovie = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                //while (movie){
                //webcam.open();
                    for (int i = 0;i < 500;i++){
                        BufferedImage image = ConverterFactory.convertToType(webcam.getImage(),BufferedImage.TYPE_3BYTE_BGR);
                        IConverter converter = ConverterFactory.createConverter(image,IPixelFormat.Type.YUV420P);
                        IVideoPicture frame = converter.toPicture(image,(System.currentTimeMillis() - start)*1000);
                        frame.setKeyFrame(i==0);
                        frame.setQuality(100);
                        writer.encodeVideo(0,frame);

                        Thread.sleep(20);
                    }
                    writer.close();
                    System.out.println("Video recored");
                //}
                return null;
            }
        };
        Thread thmv = new Thread(taskMovie);
        //thmv.setDaemon(true);
        thmv.start();
    }

}