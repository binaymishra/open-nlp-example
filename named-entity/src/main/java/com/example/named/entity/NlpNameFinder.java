package com.example.named.entity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.NameSample;
import opennlp.tools.namefind.NameSampleDataStream;
import opennlp.tools.namefind.TokenNameFinderFactory;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.MarkableFileInputStreamFactory;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;
import opennlp.tools.util.Span;
import opennlp.tools.util.StringUtil;
import opennlp.tools.util.TrainingParameters;

public class NlpNameFinder {

  private String modelFilePath;

  private String trainingFilePath;

  NlpNameFinder(String modelFilePath) {
    this.modelFilePath = modelFilePath;
  }

  NlpNameFinder(String modelFilePath, String trainingFilePath) {
    if(StringUtil.isEmpty(modelFilePath))
      throw new RuntimeException(String.format("required,  modelFilePath = %s", modelFilePath));
    if(StringUtil.isEmpty(trainingFilePath))
      throw new RuntimeException(String.format("required,  trainingFilePath = %s", trainingFilePath));
    this.modelFilePath  = modelFilePath;
    this.trainingFilePath = trainingFilePath;
  }

  public List<String> findNames(String[] tokens) throws IOException{
    List<String> names = new ArrayList<>(tokens.length);
    InputStream modelIn = null;
    try{
      modelIn = new FileInputStream(modelFilePath);
      NameFinderME nameFinder =  new NameFinderME(new TokenNameFinderModel(modelIn));
      Span[] spans = nameFinder.find(tokens);
      if(spans == null || spans.length <= 0)
        throw new RuntimeException(String.format("Name not found. spans.length = %d", spans.length));
      for (Span span : spans){
        names.add(tokens[span.getStart()]);
      }
      nameFinder.clearAdaptiveData();
      return names;
    }finally {
      if(modelIn != null){
        modelIn.close();
      }
    }
  }


  public void trainModel() throws IOException{
    Charset charset = Charset.forName("UTF-8");
    ObjectStream<String> in = new PlainTextByLineStream(new MarkableFileInputStreamFactory(new File(trainingFilePath)), charset);
    ObjectStream<NameSample> samples = new NameSampleDataStream(in);
    TokenNameFinderModel model;
    try{
      model = NameFinderME.train("en", "medicine", samples, TrainingParameters.defaultParams(),new TokenNameFinderFactory());
    }finally {
      samples.close();
      in.close();
    }

    BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream(new File(modelFilePath)));
    try{
      model.serialize(modelOut);
    }finally {
      modelOut.flush();
      modelOut.close();
    }
  }

}
