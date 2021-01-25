package com.awang.WMI.service;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**

* @author awang
* @version 1.0
* @date 2021年1月7日 上午11:25:58
* 
*/

public class ConnectAndGetInfo extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	 
    private JButton btn = null;
    private JTextField textField = null;
    private WMIByJacob wmi;
 
    public ConnectAndGetInfo() {
        this.setTitle("选择文件窗口");
        FlowLayout layout = new FlowLayout();// 布局
        JLabel label = new JLabel("请选择文件：");// 标签
        textField = new JTextField(30);// 文本域
        btn = new JButton("浏览");// 钮1
 
        // 设置布局
        layout.setAlignment(FlowLayout.LEFT);// 左对齐
        this.setLayout(layout);
        this.setBounds(400, 200, 600, 70);
        this.setVisible(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        btn.addActionListener(this);
        this.add(label);
        this.add(textField);
        this.add(btn);
 
    }
 
    public static void main(String[] args) {
    	new ConnectAndGetInfo();
	}
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	//错误输出到文件中
    	String fileName = "error.txt";
		File errorFile = new File(fileName);
		
		try {
			if(!errorFile.exists()){
				errorFile.createNewFile(); 
			}
			
			FileOutputStream fos  = new FileOutputStream(fileName);
			PrintStream ps = new PrintStream(fos);
			System.setErr(ps);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
    	JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		if (file != null) {
			String filePath = file.getPath();
			if (file.isDirectory() || !(filePath.endsWith("xls") || filePath.endsWith("xlsx"))) {
				JOptionPane.showMessageDialog(null, "请选择Excel文件", "文件选择错误",JOptionPane.WARNING_MESSAGE);
			} else if (file.isFile()) {
				this.dispose();
				Iterator<Row> iter = null;
				try {
					Workbook sheets;
					sheets = WorkbookFactory.create(file);
					//下面代码无法解决创建的xls文件改后缀名为xlsx的情况
//					if(filePath.endsWith("xls")) {							//xls文件
//						POIFSFileSystem fs = new POIFSFileSystem(file);
//						sheets = new HSSFWorkbook(fs);
//					} else {
//						sheets = new XSSFWorkbook(file);					//xlsx文件
//					}
					Sheet sheet = sheets.getSheetAt(0);
					iter = sheet.rowIterator();
					sheets.close();
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("失败");
				}
				boolean first = true;
				Row row;
				int index = 1;
				if (iter != null) {
					while (iter.hasNext()) {			//会自动跳过空行，即使在表中间
						try {
							row = iter.next();
							if (first) {			//表头跳过
								first = false;
								continue;
							}
							//getFirstCellNum()从下标0开始，getLastCellNum()从下标1开始
//						System.out.println(row.getFirstCellNum() + "--" + row.getLastCellNum());
							
							System.out.print(index++ + "：");
							if (row.getFirstCellNum() != 0) {					//第一列为空时
								System.out.println("：电脑名或IP不能为空！");
								continue;
							}
							if (row.getLastCellNum() == 1) {					//最后一列为1列时
								wmi = new WMIByJacob(row.getCell(0).toString().trim());
							} else {											//最后一列不为1时
								if (row.getCell(1) == null || row.getCell(2) == null) {  //当第二列或第三列为空时
									System.out.println("：输入用户名或密码错误");
									continue;
								}
								wmi = new WMIByJacob(row.getCell(0).toString().trim(),
										row.getCell(1).toString().trim(), row.getCell(2).toString().trim());
							}
							boolean ifGet = wmi.getComputerInfo();
							if (ifGet) {
								System.out.println("成功");
							}
						} catch (Exception ex) {
							ex.printStackTrace();
							System.out.println("失败");
						}
					}
				}
			}			
		}
    }
    
}
