package sample;

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
    private File moviefile;
    private boolean photo = false;
    private boolean movie = false;
    private Dimension camerasize = null;
    private Date date = null;
    private Queue<BufferedImage> picImages;
    private BufferedImage Images;
    private final int writetime_5000 = 5000;
    private boolean picflag = false;
    private boolean takenflag = true;
    private boolean wavflag = false;
    //Audio
    private float sample_rate = 44100;
    private int sample_size_byte = 2;
    private int channels = 2;
    private boolean signed = true;
    private boolean big_endian = false;
    private AudioFormat format;
    private TargetDataLine line;
    private File wavFile;
    private AudioFileFormat.Type fileType;
    private AudioInputStream ais;

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

    public Controller() {
        date = new Date();
        moviefile = new File("output.mp4");
        wavFile = new File("wav/test.wav");
        System.out.println(moviefile.getName());
        System.out.println(date.toString());
        picImages = new ArrayDeque<>();
        format = new AudioFormat(sample_rate, sample_size_byte * 8, channels, signed, big_endian);
        fileType = AudioFileFormat.Type.WAVE;

    }

    @FXML
    protected void startStream(ActionEvent event) {
        try {
            webcam = Webcam.getDefault();
            camerasize = WebcamResolution.VGA.getSize();
            webcam.setViewSize(camerasize);
            System.out.println(webcam.getName());
            line = AudioSystem.getTargetDataLine(format);

        } catch (Exception e) {
            System.out.println("Not found");
            return;
        }
        photo = true;
        startButton.setDisable(true);
        endButton.setDisable(false);
        movieButton.setDisable(false);
        Task<Void> recordWav = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                // while (wavflag) {
                try {
                    System.out.println("Written");
                    line.start();
                    ais = new AudioInputStream(line);
                    AudioSystem.write(ais, fileType, wavFile);
                } catch (IOException ioe) {
                    System.out.println("SystemIO Error");
                }
                //  }
                return null;
            }
        };
        Task<Void> taskPhoto = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                System.out.println("task start");
                webcam.open();
                line.open();
                while (photo) {
                    try {
                        if (wavflag) {
                            System.out.println("wavstart");
                            thwav.start();

                        }
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

        thwav = new Thread(recordWav);
        thpht = new Thread(taskPhoto);
        thpht.start();
        line.stop();
        line.close();
        wavflag = false;
    }

    @FXML
    protected void endStream(ActionEvent event) {
        startButton.setDisable(false);
        endButton.setDisable(true);
        movieButton.setDisable(true);
        photo = false;
    }

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
        Task<Void> taskMovie = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                long cnt = 0l;
                while (movie) {
                    cnt++;
                    //写真を取得して書き込みする
                    picflag = true;
                    wavflag = true;
                    Thread.sleep(writetime_5000);
                    try {
                        String num = String.valueOf(cnt);
                        if (takenflag) {
                            Ftaken1.setImage(SwingFXUtils.toFXImage(picImages.peek(), null));
                            Ftext1.setText("takenPicture[" + num + "]");
                            takenflag = false;
                        } else {
                            Ftaken2.setImage(SwingFXUtils.toFXImage(picImages.peek(), null));
                            Ftext2.setText("takenPicture[" + num + "]");
                            takenflag = true;
                        }
                        ImageIO.write(picImages.poll(), "PNG", new File("pictures/photo" + num + ".png"));
                        System.out.println(picImages);
                        System.out.println("take a picture,No." + num + "!!");
                    } catch (Exception e) {
                        System.out.println("Error!!!");
                    }
                }
                wavflag = false;
                return null;
            }
        };
        Thread thmv = new Thread(taskMovie);
        thmv.setDaemon(true);
        thmv.start();
    }

}