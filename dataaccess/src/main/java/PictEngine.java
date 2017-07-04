import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * Created by Micahel on 03.07.2017.
 */

public class PictEngine {
    public byte[] toArray(String fileName, int targetW, int targetH){
        int s = 0;
        byte[] pictBody = null;
        File image = new File(fileName);
        try(FileInputStream fis=new FileInputStream(fileName))
        {
            int pictSize = fis.available();
            pictBody = new byte[pictSize];
            fis.read(pictBody, 0, pictSize);
        }
        catch(IOException ex){

            System.err.println(ex.getMessage());
        }
        pictBody = resize(pictBody, targetW, targetH, getExtension(fileName));
        return pictBody;
    }

    public String getExtension(String fileName){
        String[] parts = fileName.split("\\.");
        String result = null;
        if ((parts!=null)&&(parts.length>1)) result = parts[parts.length - 1];
        return result;
    }

    public void toFile(int id, String fileName, String outDir, byte[] pictBody){
        String outName = outDir + '\\' + id + '.' + getExtension(fileName);
        try(FileOutputStream fos=new FileOutputStream(outName))
        {
            fos.write(pictBody, 0, pictBody.length);
        }
        catch(IOException ex){
            System.err.println(ex.getMessage());
        }
    }

    public byte[] resize(byte[] pictBody, int targetH, int targetW, String extension){
        BufferedImage bi = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(pictBody);
        try {
            bi = ImageIO.read(bais);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int height = bi.getHeight(), width = bi.getWidth();
        double coefWidth = (double)targetW/width, coefHeight = (double)targetH/height;
        byte[] result = pictBody;
        
        if ((coefWidth<1)||(coefHeight<1)) {
            double coefResize = (coefWidth>coefHeight) ? coefHeight : coefWidth;
            int newWidth = (int)(width*coefResize), newHeight = (int)(height*coefResize);
            Image tmp = bi.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage dimg = new BufferedImage(newWidth, newHeight, BufferedImage.SCALE_DEFAULT);

            Graphics2D g2d = dimg.createGraphics();
            g2d.drawImage(tmp, 0, 0, null);
            g2d.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(dimg, extension, baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = baos.toByteArray();
        }
        return result;
    }


}
