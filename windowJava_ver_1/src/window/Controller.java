package window;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Controller {
    private Webcam webcam;
    private boolean photo = false;
    private boolean movie = false;
    private Dimension camerasize = null;
    private Date date;
    private Queue<BufferedImage> picImages;
    private BufferedImage Images;
    private final int writetime_5000 = 5000;
    private boolean picflag = false;
    private boolean takenflag = true;
    private boolean wavflag = false;
    private float sample_rate = 44100;
    private int sample_size_byte = 2;
    private int channels = 2;
    private int mvcnt = 0;
    private boolean signed = true;
    private boolean big_endian = false;
    private AudioFormat format;
    private TargetDataLine line;
    private File wavFile;
    AudioFileFormat.Type fileType;
    private Thread thpht;
    private Thread thwav;
    //Audio
    @FXML
    Text Ftext1;
    @FXML
    Text Ftext2;
    @FXML
    ImageView Fimage;
    @FXML
    ImageView Ftaken1;
    @FXML
    ImageView Ftaken2;
    @FXML
    Button startButton;
    @FXML
    Button endButton;
    @FXML
    Button movieButton;
    @FXML
    Button soundButton;

    public Controller() throws Exception{
        date = new Date();
        System.out.println(date.toString());
        picImages = new ArrayDeque<>();
        format = new AudioFormat(sample_rate, sample_size_byte * 8, channels, signed, big_endian);
        fileType = AudioFileFormat.Type.WAVE;
        line = AudioSystem.getTargetDataLine(format);
        line.open(format);
    }
    //Task
    private Task<Void> taskRecord = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            try {
                System.out.println("Written");
                String mvnum = String.valueOf(mvcnt);
                wavFile = new File("movie/wav/movieNo."+mvnum+".wav");
                line.start();
                AudioInputStream ais = new AudioInputStream(line);
                AudioSystem.write(ais, fileType, wavFile);
            } catch (IOException ioe) {
                System.out.println("SystemIO Error");
            }
            return null;
        }
    };
    private Task<Void> taskPhoto = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            System.out.println("task start");
            webcam.open();
            while (photo) {
                try {
                    Images = webcam.getImage();
                } catch (Exception e) {
                    System.out.println("can't take");
                    return null;
                }
                if (movie) {
                    if (picflag) {
                        picflag = false;
                        picImages.add(Images);
                    }
                }
                Fimage.setImage(SwingFXUtils.toFXImage(Images, null));
                Images.flush();
            }
            return null;
        }
    };

    private Task<Void> taskMovie = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            long cnt = 0l;
            mvcnt++;
            while (movie) {
                cnt++;
                //写真を取得して書き込みする
                picflag = true;
                wavflag = true;
                Thread.sleep(writetime_5000);
                try {
                    String num = String.valueOf(cnt);
                    String mvnum = String.valueOf(mvcnt);
                    if (takenflag) {
                        Ftaken1.setImage(SwingFXUtils.toFXImage(picImages.peek(), null));
                        Ftext1.setText("takenPicture[" + num + "]");
                        takenflag = false;
                    } else {
                        Ftaken2.setImage(SwingFXUtils.toFXImage(picImages.peek(), null));
                        Ftext2.setText("takenPicture[" + num + "]");
                        takenflag = true;
                    }
                    ImageIO.write(picImages.poll(),
                            "PNG", new File(
                                    "movie/pictures/movieNo." + mvnum+"_photoNo."+ num + ".png"));
                    System.out.println(picImages);
                    System.out.println("take a picture,No." + num + "!!");
                } catch (Exception e) {
                    System.out.println("Error!!!");
                }
            }
            return null;
        }
    };
    //Task

    //録音
    @FXML
    protected void startSound(ActionEvent event) {
        if (wavflag == false) wavflag = true;
        else wavflag = false;


        if(wavflag == true) {
            thwav = new Thread(taskRecord);
            thwav.setDaemon(true);
            thwav.start();
        }
        if(wavflag == false) {
            line.stop();
            line.close();
            System.out.println("line.close()");
        }
    }
    //録音

    //写真
    @FXML
    protected void startStream(ActionEvent event) {
        try {
            webcam = Webcam.getDefault();
            camerasize = WebcamResolution.VGA.getSize();
            webcam.setViewSize(camerasize);
            System.out.println(webcam.getName());
        } catch (Exception e) {
            System.out.println("Not found");
            return;
        }
        photo = true;
        startButton.setDisable(true);
        endButton.setDisable(false);
        movieButton.setDisable(false);
        thpht = new Thread(taskPhoto);
        thpht.setDaemon(true);
        thpht.start();
    }
    //写真

    //写真終了
    @FXML
    protected void endStream(ActionEvent event) {
        startButton.setDisable(false);
        endButton.setDisable(true);
        movieButton.setDisable(true);
        photo = false;
    }
    //写真終了

    //ムービー
    @FXML
    protected void startMovie(ActionEvent event) {
        if (movie) {
            movie = false;
            startButton.setDisable(false);
            endButton.setDisable(false);
        } else {
            movie = true;
            startButton.setDisable(true);
            endButton.setDisable(true);
        }
        if(movie) {
            Thread thmv = new Thread(taskMovie);
            Thread thwv = new Thread(taskRecord);
            thmv.setDaemon(true);
            thwv.setDaemon(true);
            thwv.start();
            thmv.start();
        }else {
            line.stop();
            line.close();
            System.out.println("line.close()");
        }
    }
    //ムービー
}