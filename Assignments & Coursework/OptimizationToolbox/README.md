# Optimization Toolbox Readme

## Introduction

Welcome to the Optimization Toolbox! This collection of files is designed to assist you in exploring and solving optimization problems using Python. Whether you're a beginner or an experienced user, this toolbox provides functions and examples to analyze and optimize mathematical functions.

## General Information

### Requirements
These files were developed and tested on the Spyder scientific environment for Python. However, you can use any Python 3.8-compatible Integrated Development Environment (IDE) to run these files.

### Supported Functions
For both exercises, you can change the "function" to any of the following optimization test problems:
- Matyas function
- Himmelblau's function
- McCormick function
- or any other optimization test problem of your choice.

## Exercise 1

### Functions Overview

1. **partialderivative:**
   - Returns the partial derivative of a function with respect to one variable, holding the rest constant.

2. **gradient(partials):**
   - Transforms a list of sympy objects into a numpy matrix, representing the gradient.

3. **hessian(partials_second, cross_derivatives):**
   - Computes the Hessian matrix using Schwarz’s theorem on the second partial derivatives.

4. **stationary_points(symbols_list, partials):**
   - Finds the stationary points of a function and returns their coordinates.

5. **determat(partials_second, cross_derivatives, criticalpoint, symbols_list):**
   - Computes the determinant of the Hessian matrix at a critical point.

6. **scipy.linalg.eig:**
   - Uses the scipy library to compute eigenvalues and eigenvectors of a square matrix.

### Visualization
   - Generates 50 values with np.linspace(0, 5, 50).
   - Creates combinations of grids with np.meshgrid.
   - Draws a rectangular contour plot using plt.contour(X, Y, Z, cmap='black').

## Exercise 2

### Additional Functions

1. **Taylors:**
   - Returns a Sympy expression of the second-order Taylor series for a given multivariate expression.

2. **Start of Newton's Method:**
   - Implements Newton's Method for finding roots of a function.

3. **gradient_descent():**
   - Basic implementation of the gradient descent algorithm for optimization.

4. **Newton-Conjugate-Gradient algorithm (method='Newton-CG'):**
   - Modified Newton’s method using a conjugate gradient algorithm for inverting the local Hessian.

### Optimization with scipy.optimize
   - The scipy.optimize package provides various optimization algorithms, including Newton-Conjugate Gradient. Refer to the documentation for detailed usage.

Feel free to explore and modify these files to suit your optimization needs. Happy optimizing!
