package spotify;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import java.io.IOException;

public class MRmapper extends Mapper<Object, Text, Text, LongWritable> {
  // Input Field Separator, Output Field Separator, Number of properties
  static String IFS = ",";
  static String OFS = ",";
  static int NF = 7;


  /**
   * @value: Line in the csv file. Fomratted as
   *         'video_id,title,channel_title,category_id,tags,views,likes,dislikes,
   *         comment_total,thumbnail_link,date'
   */
  @Override
  public void map(Object key, Text value, Context context)
      throws IOException, InterruptedException {

    /**
     * [position, trackname, artist, streams, url, date, region]
     */
    String[] properties = value.toString().split(IFS);
    // 1. Checks if the row is valid
    if (!isValid(properties)) {
      return;
    }

    // 2: Initialize variables of interest
//    long position = Long.parseLong(properties[0]);
//    String trackname = properties[1];
    String artist = properties[2];
    long streams = Long.parseLong(properties[3]);
//    String url = properties[4];
//    String date = properties[5];
//    String region = properties[6];
    
    // 3: write key value pair to context
    context.write(new Text(artist), new LongWritable(streams));
  }

  // Makes sure line from csv is valid
  public boolean isValid(String[] properties) {
    if (properties.length != NF)
      return false;
    try {
      Integer.parseInt(properties[0]);
      Integer.parseInt(properties[3]);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
