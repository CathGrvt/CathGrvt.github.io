# ICC2018 Genetic Analysis Program

## Introduction

Welcome to the ICC2018 Genetic Analysis Program! This C++ program is designed to analyze genetic sequences and determine whether a phenotype is altered or preserved based on mutation probabilities and ratios.

## Program Overview

This program focuses on analyzing genetic sequences related to the K-RAS gene. It provides functions to calculate mutation probabilities, compare sequences, and assess the impact of mutations on the phenotype. The main features include:

- Analysis of mutation probability and phenotype alteration based on specified probabilities and mutation ratios.
- Representation and analysis of reference sequences for the K-RAS gene.
- Display of invalid gene sequences for debugging purposes.
- Compact representation of genetic sequences for easier analysis.
- Probability and ratio calculations for comparing genetic sequences with a reference.

## Program Structure

The program consists of the following components:

1. **Genetic Sequence Representation:**
   - Uses a typedef to define the base type as 'char' and a sequence as a 'string'.
   - Defines the 'Gene' type as an array of four sequences.

2. **Constants:**
   - Sets a threshold for the mutation ratio that determines when the number of mutations becomes dangerous.

3. **Functions:**
   - `isAT`, `isCG`, `isCT`: Check if bases form specific pairs.
   - `probability`: Calculate the probability of mutation between two bases.
   - `analyze`: Determine phenotype alteration based on mutation probability and ratio.
   - `print`: Display the gene sequences.
   - `compact`: Create a compact representation of the gene.
   - `difference`: Count the number of altered bases between two sequences.
   - `test_proba` and `test_compare`: Provided test functions for testing probability and sequence comparison.

4. **Main Function:**
   - Performs multiple tests:
     - Test 1: Analyze phenotype alteration with different probabilities and mutation ratios.
     - Test 2: Display reference sequences and invalid genes.
     - Test 3: Analyze mutation probability and ratio for a specific gene.
     - Test 4: Compare a mutated gene with the reference and analyze phenotype alteration.
     - Test 5: Mutate a reference gene, compare with the original, and analyze phenotype alteration.

## How to Use

To use the program, run it and observe the results of the provided tests. The program outputs information on phenotype alteration, mutation probabilities, and altered bases ratios.

Feel free to explore and modify the program to suit your genetic analysis needs. Happy analyzing!