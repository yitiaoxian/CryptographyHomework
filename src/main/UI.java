package main;
/*
    author xiaoqianke
    13488954741@163.com
    2020年5月6日16点32分
 */

import org.apache.commons.codec.binary.Base64;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.KeyPair;

public class UI extends JFrame implements ActionListener {

    /*
        使用的RSA加密的公钥
     */
    private static JTextArea rsaPub;
    /*
        使用的RSA加密的私钥
     */
    private static JTextArea rsaPri;
    /*
        信息加密后的结果
     */
    private static JTextArea encodedInfo;
    /*
        加密后的信息再解密
     */
    private static JTextArea decodedInfo;
    /*
        解密后的信息验证
     */
    private static JTextArea resultCheck;
    /*
        提交待加密信息的按钮
     */
    private static JButton submitInfo;
    /*
        重置信息按钮
     */
    private static JButton resetInfo;
    /*
        解密按钮
     */
    private static JButton resultCh;
    /*
        私钥标签
     */
    private static JLabel priInfo;
    /*
        公钥标签
     */
    private static JLabel pubInfo;

    public void showFrame(){

        resetInfo = new JButton("reset");
        resetInfo.addActionListener(this);
        resetInfo.setBounds(300,500,200,50);
        resetInfo.setBackground(Color.WHITE);
        resetInfo.setVisible(true);

        resultCh = new JButton("check");
        resultCh.addActionListener(this);
        resultCh.setBounds(500,500,200,50);
        resultCh.setBackground(Color.WHITE);
        resultCh.setVisible(true);

        submitInfo = new JButton("submit");
        submitInfo.addActionListener(this);
        submitInfo.setBounds(100,500,200,50);
        submitInfo.setBackground(Color.WHITE);
        submitInfo.setVisible(true);

        decodedInfo = new JTextArea("加密后的信息");
        decodedInfo.setRows(10);
        decodedInfo.setLineWrap(true);
        decodedInfo.setBounds(10,10,400,100);



        rsaPub = new JTextArea("公钥");
        rsaPub.setRows(10);
        rsaPub.setLineWrap(true);
        rsaPub.setBounds(500,10,400,200);

        pubInfo = new JLabel();
        pubInfo.setText("公钥");
        pubInfo.setBounds(700,220,40,30);

        rsaPri = new JTextArea("私钥");
        rsaPri.setRows(10);
        rsaPri.setLineWrap(true);
        rsaPri.setBounds(500,260,400,200);

        priInfo = new JLabel();
        priInfo.setText("私钥");
        priInfo.setBounds(700,470,40,30);

        encodedInfo = new JTextArea("输入需要加密的内容");
        encodedInfo.setRows(10);
        encodedInfo.setLineWrap(true);
        encodedInfo.setBounds(10,360,400,100);
        encodedInfo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                encodedInfo.setText("");
            }
        });

        resultCheck = new JTextArea("加密后的信息再解密");
        resultCheck.setLineWrap(true);
        resultCheck.setColumns(10);
        resultCheck.setBounds(10,150,400,100);

        this.add(submitInfo);
        this.add(resetInfo);
        this.add(resultCheck);

        this.add(decodedInfo);
        this.add(encodedInfo);
        this.add(resultCh);

        this.add(rsaPri);
        this.add(rsaPub);
        this.add(pubInfo);
        this.add(priInfo);

        this.setLayout(null);
        this.setSize(1000,600);
        this.setLocation(400,400);
        this.setVisible(true);
        this.setTitle("RSA加密解密验证工具");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    public void actionPerformed(ActionEvent e){
        Test t = new Test();
        if(e.getSource()==submitInfo){
            String tmp = encodedInfo.getText();
            //获取输入的字符串
            KeyPair keyPair = null;
            try {
                keyPair = t.getKeyPair();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            try {
                String encodeStr = t.encrypt(tmp,t.getPublicKey(publicKey));
                decodedInfo.setText(encodeStr);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

            rsaPub.setText(publicKey);
            rsaPri.setText(privateKey);

        }else if(e.getSource() == resetInfo){
            /*
                重置输入与输出
             */
            decodedInfo.setText("");
            encodedInfo.setText("");
            resultCheck.setText("");
            rsaPri.setText("");
            rsaPub.setText("");
        }else if(e.getSource() == resultCh){
            /*
                解密验证
             */
            String unprocessed = decodedInfo.getText();
            String strPub = rsaPub.getText();
            String strPri = rsaPri.getText();
            String data = encodedInfo.getText();

            try {
                /*
                    RSA解密
                 */
                String decryptData = t.decrypt(unprocessed,t.getPrivateKey(strPri));

            /*
                RSA签名
             */

                String sign = t.sign(data,t.getPrivateKey(strPri));
                Boolean result = t.verify(data,t.getPublicKey(strPub),sign);
                resultCheck.setText("RSA解密结果："+decryptData);
               // resultCheck.setText("RSA解密结果："+decryptData+"\n"+"RSA签名结果："+sign+"\n"+"RSA验证结果："+result);
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }
    }



    public static void main(String args[]){
       UI ui = new UI();
       ui.showFrame();

    }
}
