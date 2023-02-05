package com.wehgu.admin.utils.OSS;

import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


@Slf4j
@Component
public class ImageUtil {

    @Resource
    private OSSUtil ossUtil;

    public static byte[] yasuo_img(InputStream inputStream){
        byte[] b1=null;
        BufferedImage image=null;
        ByteArrayOutputStream out=null;
        /**
         * try catch 套用try catch 是为了获取具体发生异常的位置及原因,为了更好的标识出来
         */
        try{
            try {
                image= ImageIO.read(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
                log.error("压缩图片时,读取图片流发生异常");
                log.error(e.getMessage(),e);
                throw new RuntimeException("压缩图片时,读取图片流发生异常");
            }

            if(image==null){
                throw new RuntimeException("获取到的图片为空");
            }
            out = new ByteArrayOutputStream();
            try {
                /*
                 * size(width,height) 若图片横比1920小，高比1080小，不变 若图片横比1920小，高比1080大，高缩小到1080，图片比例不变
                 * 若图片横比1920大，高比1080小，横缩小到1920，图片比例不变 若图片横比1920大，高比1080大，图片按比例缩小，横为1920或高为1080
                 * 图片格式转化为jpg,质量不变
                 */
                if (image.getHeight() > 1080 || image.getWidth() > 1920) {
                    Thumbnails.of(image).size(1920, 1080).outputQuality(1f).outputFormat("jpg").toOutputStream(out);
                }else {
                    Thumbnails.of(image).outputQuality(1f).scale(1f).outputFormat("jpg").toOutputStream(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("压缩图片时,输出图片流发生异常");
                log.error(e.getMessage(),e);
                throw new RuntimeException("压缩图片时,输出图片流发生异常");
            }
            b1 = out.toByteArray();
        }catch (Exception ex){
            log.error("压缩图片时,发生了未知异常");
            log.error(ex.getMessage(),ex);
            //如果是运行时异常则 抛出,有专门的地方处理运行时异常
            if(ex instanceof RuntimeException){
                throw ex;
            }
        }finally {
            try {
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("压缩图片时,输出图片流资源释放发生异常");
                log.error(e.getMessage(),e);
                throw new RuntimeException("压缩图片时,输出图片流资源释放发生异常");
            }
        }
        return b1;
    }


    public static byte[] inputStreamToByte(InputStream inputStream) throws IOException {
        byte[] b1=new byte[90000000];
        int rc =0 ;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        while(( rc =  inputStream.read(b1,0,90000000) )>0 )
        {
              baos.write(b1,0,rc);
        }
        return b1;
    }



    public static BufferedImage byte2Image(byte[] bytes){
        ByteArrayInputStream bais =null;
        BufferedImage bi1=null;
        try {
            bais=new ByteArrayInputStream(bytes);
            bi1 =ImageIO.read(bais);
        }catch (Exception ex){
            log.error("二进制转图片发生异常");
            log.error(ex.getMessage(),ex);
        }finally {
            if(bais!=null){
                try {
                    bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bi1;
    }

    public static BufferedImage rotateImage(BufferedImage bufferedImage,Double angel){
        if(bufferedImage==null){
            return null;
        }
        int w = bufferedImage.getWidth();
        int h = bufferedImage.getHeight();

        // 计算旋转后图片的尺寸
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(w, h)), angel.intValue());
//        int type = bufferedImage.getColorModel().getTransparency();
//        BufferedImage img;
//        Graphics2D graphics2d;
//        (graphics2d = (img = new BufferedImage(h, w, type)).createGraphics()).setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        graphics2d.rotate(angel, w / 2, h / 2 + (w>h?(w-h)/2:(h-w)/2));
//        graphics2d.drawImage(bufferedImage, 0, 0, null);
//        graphics2d.dispose();
//        return img;

        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // 进行转换
        g2.translate((rect_des.width - w) / 2,
                (rect_des.height - h) / 2);
        g2.rotate(Math.toRadians(angel), w / 2, h / 2);

        g2.drawImage(bufferedImage, null, null);
        return res;



    }

    public static byte[] bufferedImage2ByteArray(BufferedImage bufferedImage){
        byte[] bytes=null;
        if(bufferedImage==null){
            return null;
        }
        ByteArrayOutputStream   out = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage,"jpg",out);
            bytes=out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    /**
     * 计算旋转后的图片
     *
     * @param src   被旋转的图片
     * @param angel 旋转角度
     * @return 旋转后的图片
     */
    public static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // 如果旋转的角度大于90度做相应的转换
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angel_dalta_width = Math.atan((double) src.height / src.width);
        double angel_dalta_height = Math.atan((double) src.width / src.height);

        int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_width));
        int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha
                - angel_dalta_height));
        int des_width = src.width + len_dalta_width * 2;
        int des_height = src.height + len_dalta_height * 2;
        return new Rectangle(new Dimension(des_width, des_height));
    }

    public String savePictureToOss(String dir, MultipartFile multipartFile, String mainmsg) {
        String save_path="";
        InputStream sbs=null;
        InputStream inputStream=null;

        try{
            try {
                if (multipartFile == null || multipartFile.getBytes() == null || multipartFile.getBytes().length == 0) {
                    throw new RuntimeException(String.format("%s获取图片字节信息发生异常,保存失败",mainmsg));
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("保存图片到oss发生异常");
                log.error(String.format("%s,获取图片流信息发生异常",mainmsg),e);
                throw new RuntimeException(String.format("%s,获取图片流信息发生异常,保存失败",mainmsg));
            }
            try {
                //todo 获得上传文件中文件流。
                inputStream=multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("保存图片到oss发生异常");
                log.error(String.format("%s,获取图片流信息发生异常",mainmsg),e);
                throw new RuntimeException(String.format("%s,获取图片流信息发生异常,保存失败",mainmsg));
            }
            /*
              压缩图片
             */
            byte[]   b1= yasuo_img(inputStream);


            save_path = ossUtil.generateFilePath(dir, multipartFile.getOriginalFilename());
            System.out.println(save_path);

            sbs = new ByteArrayInputStream(b1);
            String upload_eTag = ossUtil.uploadFile2OSS(sbs, save_path);
            if(StringUtils.isBlank(upload_eTag)){
                throw new RuntimeException("图片上传到oss发生异常");
            }
        }catch (Exception ex){
            log.error("上传图片到oss时,发生了未知异常");
            log.error(ex.getMessage(),ex);
            //如果是运行时异常则 抛出,有专门的地方处理运行时异常
            if(ex instanceof RuntimeException){
                throw ex;
            }
        }finally {
            if(sbs!=null){
                try {
                    sbs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                    log.error("保存图片到oss发生异常");
                    log.error(String.format("%s,用于上传到oss所创建的图片流释放发生异常,保存失败",mainmsg),e);
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("保存图片到oss发生异常");
                    log.error(String.format("%s,接收的图片流释放发生异常,保存失败",mainmsg),e);
                }
            }
        }
        return save_path;
    }


    public String saveVideoToOss(String dir, MultipartFile multipartFile, String mainmsg) {
        String save_path="";
        InputStream sbs=null;
        InputStream inputStream=null;

        try{
            try {
                if (multipartFile == null || multipartFile.getBytes() == null || multipartFile.getBytes().length == 0) {
                    throw new RuntimeException(String.format("%s获取图片字节信息发生异常,保存失败",mainmsg));
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.error("保存图片到oss发生异常");
                log.error(String.format("%s,获取图片流信息发生异常",mainmsg),e);
                throw new RuntimeException(String.format("%s,获取图片流信息发生异常,保存失败",mainmsg));
            }
            try {
                //todo 获得上传文件中文件流。
                inputStream=multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("保存图片到oss发生异常");
                log.error(String.format("%s,获取图片流信息发生异常",mainmsg),e);
                throw new RuntimeException(String.format("%s,获取图片流信息发生异常,保存失败",mainmsg));
            }


            save_path = ossUtil.generateFilePath(dir, multipartFile.getOriginalFilename());
            System.out.println(save_path);

            String upload_eTag = ossUtil.uploadFile2OSS(inputStream, save_path);
            if(StringUtils.isBlank(upload_eTag)){
                throw new RuntimeException("图片上传到oss发生异常");
            }
        }catch (Exception ex){
            log.error("上传图片到oss时,发生了未知异常");
            log.error(ex.getMessage(),ex);
            //如果是运行时异常则 抛出,有专门的地方处理运行时异常
            if(ex instanceof RuntimeException){
                ;
            }
        }finally {
            if(sbs!=null){
                try {
                    sbs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    e.printStackTrace();
                    log.error("保存图片到oss发生异常");
                    log.error(String.format("%s,用于上传到oss所创建的图片流释放发生异常,保存失败",mainmsg),e);
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    log.error("保存图片到oss发生异常");
                    log.error(String.format("%s,接收的图片流释放发生异常,保存失败",mainmsg),e);
                }
            }
        }
        return save_path;
    }


}