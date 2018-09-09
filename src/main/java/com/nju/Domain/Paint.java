package com.nju.Domain;

import com.baidu.aip.imageclassify.AipImageClassify;
import com.nju.Service.BaiduAuthService;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

/**
 * @Author shisj
 * @Date: 2018/9/6 16:49
 */
public class Paint {

    private ArrayList<Stroke> shapes = new ArrayList<>();

    private ArrayList<TagRect> tags = new ArrayList<>();

    public ArrayList<Stroke> getShapes() {
        return shapes;
    }

    public void setShapes(ArrayList<Stroke> shapes) {
        this.shapes = shapes;
    }

    public ArrayList<TagRect> getTags() {
        return tags;
    }

    public void setTags(ArrayList<TagRect> tags) {
        this.tags = tags;
    }

    /**
     * 撤销上一笔
     * @param graphicsContext
     */
    public void resetLastStep(GraphicsContext graphicsContext){
        if(shapes.size() == 0){
            return;
        }
        Stroke tmpStroke = shapes.get(shapes.size()-1);
        tmpStroke.clearStroke(graphicsContext, graphicsContext.getLineWidth());
        shapes.remove(shapes.size()-1);
        /*
        处理重合线条
         */
        restoreAll(graphicsContext);
    }

    /**
     * 撤销上一个标注
     * @param graphicsContext
     */
    public void resetLastRect(GraphicsContext graphicsContext){
        if(tags.size() == 0){
            return;
        }
        TagRect tmpRect = tags.get(tags.size() - 1);
        tmpRect.clearRect(graphicsContext);
        tags.remove(tags.size() - 1);
        restoreAll(graphicsContext);
    }

    /**
     * 重绘当前画布所有内容
     * @param graphicsContext
     */
    public void restoreAll(GraphicsContext graphicsContext){
        restoreRects(graphicsContext);
        restoreStrokes(graphicsContext);
    }

    /**
     * 存储标注部分为图片
     * @param canvas
     * @param graphicsContext
     * @param client
     * @param snapthotRect
     * @return
     */
    public String saveAsPng(Canvas canvas, GraphicsContext graphicsContext, Rectangle snapthotRect){
        WritableImage wim = new WritableImage(600,450);
        canvas.snapshot(null, wim);

        File file = new File(getClass().getClassLoader().getResource("img/test.png").getPath());
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(wim,null), "png", file);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(cutImage(file.getPath(), file.getPath(), snapthotRect)){
            BaiduAuthService.getAuth();
            return "";
        }
        //非法标注清除标框
        TagRect rect = new TagRect();
        rect.setOriginPoint(new Point(snapthotRect.getX(), snapthotRect.getY()));
        rect.setWidth(snapthotRect.getWidth());
        rect.setHeight(snapthotRect.getHeight());
        rect.clearRect(graphicsContext);
        return null;
    }

    private void restoreStrokes(GraphicsContext graphicsContext){
        for(Stroke stroke: shapes){
            stroke.drawStroke(graphicsContext);
        }
    }

    private void restoreRects(GraphicsContext graphicsContext){
        for (TagRect tagRect: tags){
            tagRect.clearRect(graphicsContext);
        }
        for(TagRect tagRect: tags){
            tagRect.drawRect(graphicsContext);
        }
    }

    private boolean cutImage(String src, String dest, java.awt.Rectangle snapshotRect){
        File srcFile = new File(src);
        File destFile = new File(dest);
        try {
            FileInputStream fileInputStream= new FileInputStream(srcFile);
            ImageInputStream imageInputStream = ImageIO.createImageInputStream(fileInputStream);
            ImageReader reader = ImageIO.getImageReadersBySuffix("png").next();
            reader.setInput(imageInputStream, true);
            ImageReadParam param = reader.getDefaultReadParam();
            param.setSourceRegion(snapshotRect);//截取
            BufferedImage bufferedImage = reader.read(0, param);
            fileInputStream.close();
            FileOutputStream fileOutputStream= new FileOutputStream(destFile.getPath());
            ImageIO.write(bufferedImage, "png", fileOutputStream);
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalArgumentException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("非法标注框");
            alert.showAndWait();
            return false;
        }
        return true;
    }

}
