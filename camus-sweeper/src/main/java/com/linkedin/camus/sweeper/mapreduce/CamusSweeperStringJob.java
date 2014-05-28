package com.linkedin.camus.sweeper.mapreduce;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.mapred.AvroKey;
import org.apache.avro.mapred.AvroOutputFormat;
import org.apache.avro.mapred.AvroValue;
import org.apache.avro.mapred.FsInput;
import org.apache.avro.mapreduce.AvroJob;
import org.apache.avro.mapreduce.AvroKeyOutputFormat;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.CombineFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineTextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.input.CombineFileRecordReader;
import org.apache.hadoop.mapreduce.lib.input.CombineFileSplit;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.fieldsel.FieldSelectionMapper;
import org.apache.log4j.Logger;

public class CamusSweeperStringJob extends CamusSweeperJob
{
  @Override
  public void configureJob(String topic, Job job)
  {
    // setting up our input format and map output types
    super.configureInput(job, TextInputFormat.class, FieldSelectionMapper.class, Text.class, Text.class);

    // setting up our output format and output types
    super.configureOutput(job, TextOutputFormat.class, StringReducer.class, Text.class, NullWritable.class);

    job.getConfiguration().set("mapreduce.fieldsel.map.output.key.value.fields.spec",
			       System.getProperty("mapreduce.fieldsel.map.output.key.value.fields.spec", "0-2:0-2"));
  }
}
