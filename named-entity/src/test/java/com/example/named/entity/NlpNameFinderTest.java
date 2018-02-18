package com.example.named.entity;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class NlpNameFinderTest {

  private NlpTokenizer tokenizer;

  private NlpNameFinder nameFinder;

  @Before
  public void setup() throws Exception{
    tokenizer  = new NlpTokenizer("en-token.bin");
    nameFinder = new NlpNameFinder("en-ner-person.bin");
  }

  @Test
  public void findNames() throws Exception{
    final String input = "Where is Charlie and Mike.";
    List<String> names = nameFinder.findNames(tokenizer.tokenize(input));
    names.forEach(System.out::println);
  }


  @Test
  public void findNamesFromMultipleLines() throws Exception{
    String[] sentences = {
        "Where is Charlie and Mike.",
        "Former first lady Nancy Reagan was taken to a suburban Los Angeles hospital as a precaution Sunday after a fall at her home, an aide said.",
        "Bill Clinton was the president before Bush"
    };
    List<String> names = new ArrayList<>();
    for (String s: sentences){
      names.addAll(nameFinder.findNames(tokenizer.tokenize(s)));
    }
    names.forEach(System.out::println);
  }


  @Test
  public void trainDrugModel() throws Exception{
    nameFinder = new NlpNameFinder("en-ner-drugs.bin", "en-ner-drugs.train");
    nameFinder.trainModel();
  }

  @Test
  public void findDrugNames() throws Exception{
    final String input = "Augmentin-Duo is a good medicine.";
    tokenizer  = new NlpTokenizer("en-token.bin");
    nameFinder = new NlpNameFinder("en-ner-drugs.bin");
    List<String> names = nameFinder.findNames(tokenizer.tokenize(input));
    names.forEach(System.out::println);
  }

}
