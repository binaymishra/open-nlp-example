package com.example.named.entity;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class NlpTokenizer {

  private String modelFilePath;

  NlpTokenizer(String modelFilePath) throws IOException{
   this.modelFilePath = modelFilePath;
  }

  public String[] tokenize(String paragraph) throws IOException{
    if(null == paragraph || "".equals(paragraph))
      throw new IllegalArgumentException(String.format("paragraph = %s", paragraph));
    InputStream modelIn = null;
    try{
      modelIn = new FileInputStream(modelFilePath);
      TokenizerModel tokenizerModel = new TokenizerModel(modelIn);
      Tokenizer tokenizer = new TokenizerME(tokenizerModel);
      return tokenizer.tokenize(paragraph);
    }finally {
      if(modelIn != null){
        modelIn.close();
      }
    }
  }

}
