package com.linkedin.camus.sweeper.mapreduce;

import java.io.IOException;
import java.util.Iterator;

import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroValue;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class StringReducer extends Reducer<Text, Text, Text, NullWritable>
{
  private Text outKey;

  @Override
  protected void setup(Context context) throws IOException,
      InterruptedException
  {
    outKey = new Text();
  }

  @Override
  protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException,
      InterruptedException
  {
    System.out.println("key: " + key);
    System.out.println("values: " + values);
    int numVals = 0;
    Iterator<Text> iter = values.iterator();
    assert iter.hasNext();
    Text cur = iter.next();
    System.out.println("setting value " + cur);
    for (outKey.set(cur);
	 iter.hasNext();
	 cur = iter.next()) {
      System.out.println("duplicate value " + cur);
      numVals++;
    }

    if (numVals > 1)
    {
      context.getCounter("EventCounter", "More_Than_1").increment(1);
      context.getCounter("EventCounter", "Deduped").increment(numVals - 1);
    }

    System.out.println("writing " + outKey);

    context.write(outKey, NullWritable.get());
  }
}
