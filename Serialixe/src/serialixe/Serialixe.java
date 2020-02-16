/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package serialixe;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Yamamoto
 */
public class Serialixe {
    
    private static Calendar cl = Calendar.getInstance();
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    
    final static int HEADER_SIZE = 1024;
    final static int BODY_SIZE = 1024;
    final static int MAX_INDEX = 1024;
    final static int SECTION_NUM = 5;
    
    private static int SectionUpperLimit[] = new int[SECTION_NUM];
    private static int SectionLowerLimit[] = new int[SECTION_NUM];
    private static int SectionContinuty[] = new int[SECTION_NUM];
    private static int SectionStartValue[] = new int[SECTION_NUM];
    private static int SectionTime[] = new int[SECTION_NUM];
    private static int SectionTolerance[] = new int[SECTION_NUM];
    
    public static void main(String[] args) throws IOException {

        String inFile = "IMG_0133.jpeg";
        String csvFile = "test.csv";
        
        // body部構造
        int index = 0;
        int OriginalIndex[] = new int[MAX_INDEX];
        int getIntValue1[] = new int[MAX_INDEX];
        int getIntValue2[] = new int[MAX_INDEX];
        short getShortValue[] = new short[MAX_INDEX];
        int sectionStart[] = new int[SECTION_NUM];
        int dataValid = 0;
        
        try(FileInputStream fis = new FileInputStream(inFile);
			      BufferedInputStream bis = new BufferedInputStream(fis)){
          
			          ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] header = new byte[HEADER_SIZE];
		   	        byte[] data = new byte[BODY_SIZE];
			          // Header分読み飛ばし
                int n = bis.read(header);
			          int currentSection = -1;
                int errorCnt = 0;
                int latestIndex = 0;
                
                while(n != -1) {
				            
                    if (index >= MAX_INDEX) break;
                    
				            n = bis.read(data);
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    
                    // get value from binay data
                    OriginalIndex[index] = index;
                    
                    // 工学値変換
                    getShortValue[index] = byteBuffer.getShort(0);
                    getIntValue1[index] = byteBuffer.getInt(2);
                    getIntValue2[index] = byteBuffer.getInt(6);
                    
                    // インデックスの並びを確認
                    if ( getShortValue[index] != getShortValue[index-1] + 1 ) {
                        dataValid += 1;
                    }
                    
                    // セクション開始位置を保存
                    currentSection = -1;
                    for( int i = 0; i < SECTION_NUM; i++ ){
                        if ( SectionStartValue[i] < getIntValue1[index] && sectionStart[i] == 0 ) { 
                            sectionStart[i] = index;
                            currentSection = i;
                        }
                    }
                    
                    // 上限下限チェック
                    if ( currentSection != -1) {
                        if ( getIntValue1[index] > SectionUpperLimit[currentSection] && latestIndex == index + 1 ) {
                            errorCnt++;
                            latestIndex = index;
                        } else if ( getIntValue1[index] < SectionLowerLimit[currentSection] && latestIndex == index + 1 ) {
                            errorCnt++;
                            latestIndex = index;
                        } else {
                            errorCnt = 0;
                        }
                    }
                    if ( errorCnt > SectionContinuty[currentSection] ) {
                      dataValid += 2;
                    }
                    
                    index++;
			          }
        
		    } catch ( IOException e ) {
			      e.printStackTrace();
		    }
        
        // csv化
        exportCsv( csvFile, getIntValue1, getShortValue, getIntValue2 );
        
        // 異常判定
        if ( dataValid == 1 ) {
            System.out.println("index is invaild !!!");      
        } else if ( dataValid == 2 ) {
            System.out.println("Upper Lower Limit error !!!");     
        } else if ( dataValid == 3 ) {
            System.out.println("Section time is invalid !!!");     
        }else {
            System.out.println("Success to cleasing !!!");
            
        }
        
    }
    
    public static void exportCsv( String fileName, int[] index, short[] aList, int[] bList ){
     
        try {
            // 出力ファイルの作成
            FileWriter fWriter = new FileWriter(fileName, false);
            PrintWriter pWriter = new PrintWriter(new BufferedWriter(fWriter));
 
            // ヘッダーを指定する
            pWriter.print("date");
            pWriter.print(",");
            pWriter.print("index");
            pWriter.print(",");
            pWriter.print("a value");
            pWriter.print(",");
            pWriter.print("b value");
            pWriter.println();
 
            // 出力内容をセットする
            for(int i = 0; i < index.length; i++){
                pWriter.print(sdf.format(cl.getTime()));
                pWriter.print(",");
                pWriter.print(index[i]);
                pWriter.print(",");
                pWriter.print(aList[i]);
                pWriter.print(",");
                pWriter.print(bList[i]);
                pWriter.println();
            }
 
            // ファイルに書き出し閉じる
            pWriter.close();
 
            System.out.println("ファイル出力完了！");
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
         
    }
}