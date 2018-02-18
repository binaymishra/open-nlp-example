package com.example.named.entity;

import java.util.Arrays;
import org.junit.Test;

public class NlpTokenizerTest {

  @Test
  public void tokenize() throws Exception{
    NlpTokenizer tokenizer = new NlpTokenizer("en-token.bin");
    String[] sentences = {
        "Where is Charlie and Mike.",
        //"Former first lady Nancy Reagan was taken to a suburban Los Angeles hospital as a precaution Sunday after a fall at her home, an aide said."
    };
    for (String s: sentences){
          System.out.println(Arrays.toString(tokenizer.tokenize(s)));
      }

  }

}
