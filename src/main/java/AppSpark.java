

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.ml.feature.Word2Vec;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.ArrayType;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import com.fasterxml.jackson.module.scala.util.Strings;

public final class  AppSpark{
  private static final Pattern SPACE = Pattern.compile(" ");

  public static void main(String[] args) throws Exception {	  	

	  LinkedList<String> lotr = readFile2("data\\poprawna_lematyzacja.txt");
	   
	  
	  SparkSession spark = SparkSession
			  .builder()
			  .appName("JavaALSExample")
			  .config("spark.sql.warehouse.dir", "file:///C:/tmp/spark-warehouse")
			  .master("local[2]")
			  .getOrCreate();
	  		
	    	    List<Row> data = new LinkedList<Row>();
	    	    
	    	    for(String s : lotr){
	    	    	data.add(RowFactory.create(Arrays.asList(s.split(" "))));
	    	    	}
	    		
	  			/*List<Row> data = Arrays.asList(
	  				  RowFactory.create(Arrays.asList("Hi I heard about Spark".split(" "))),
	  				  RowFactory.create(Arrays.asList("I wish Java could use case classes".split(" "))),
	  				  RowFactory.create(Arrays.asList("Logistic regression models are neat".split(" ")))
	  				  
	  				); 
	    	    */
	    	   
	    	    StructType schema = new StructType(new StructField[]{
	    	      new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
	    	    });
	    	
	    	    Dataset<Row> documentDF = spark.createDataFrame(data, schema);
	    	 
	    	    // Learn a mapping from words to Vectors.
	    	    Word2Vec word2Vec = new Word2Vec()
	    	      .setInputCol("text")
	    	      .setOutputCol("result")
	    	      .setVectorSize(1)
	    	      .setMinCount(0);
	    	    Word2VecModel model = word2Vec.fit(documentDF);
	    	    Dataset<Row> result = model.transform(documentDF);
	    	    
	    	    model.findSynonyms("policjant", 10).show();    	 

	    	    spark.stop();
	  }

  
  public static LinkedList<String> readFile(String title) {
	    byte[] encoded;
	    
	    LinkedList<String> s = new LinkedList<String>();
		try {
			encoded = Files.readAllBytes(Paths.get(title));
		    String res = new String(encoded, StandardCharsets.UTF_8);	
		    
		  	s.addAll(Arrays.asList(res.split("\\.")));
		} catch (IOException e1) {
			e1.printStackTrace();}	
		  return s;
}
  
  public static LinkedList<String> readFile2(String title) {
	    byte[] encoded;
	    
	    LinkedList<String> s = new LinkedList<String>();
		try {
			String line = "";
			
			FileInputStream fstream = new FileInputStream(title);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF8"));			
			while ((line = br.readLine()) != null)   {
				s.add(line);
				}
		 
			br.close();
		} catch (IOException e1) {
			e1.printStackTrace();}	
		  return s;
}
  

}
