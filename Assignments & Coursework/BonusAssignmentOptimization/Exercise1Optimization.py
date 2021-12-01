# -*- coding: utf-8 -*-
"""
Created on Thu Oct 14 2021

@author: grivot
"""

#exercise 1

import numpy as np
import matplotlib.pyplot as plt
from sympy import symbols, Eq, solve
from sympy.utilities.lambdify import lambdify

def partialderivative(element, function):
	
    """
	partialderivative is basically sympy.core.symbol.Symbol * sympy.core.add.Add
    It gives the partial derivative of a function with several variables is its derivative,
    with respect to one of those variables while holding the rest constant 
	"""
    deri = function.diff(element)

    return deri


def gradient(partials):
    
	"""
	gradient(partials) makes the List[sympy.core.add.Add] and hence a numpy.matrix
	So it transforms a list of sympy objects into a numpy matrix
	"""
	grad = np.matrix([[partials[0]], [partials[1]]])

	return grad

def hessian(partials_second, cross_derivatives):
    
	"""
	hessian(partials_second, cross_derivatives) gives a List[sympy.core.add.Add] * sympy.core.add.Add 
    hence we get a numpy.matrix
	the same way as the gradient it transforms a list of sympy objects into a numpy hessian matrix
    I use the Schwarz’s theorem on the second partial derivatives of a function of several variables
	"""
	hessianmatrix = np.matrix([[partials_second[0], cross_derivatives], [cross_derivatives, partials_second[1]]])

	return hessianmatrix


def stationary_points(symbols_list, partials):
    
	"""
	stationary_points gives a List[sympy.core.symbol.Symbol] * List[sympy.core.add.Add] 
    hence we have a Dict[sympy.core.numbers.Float]
	stationary_points(symbols_list, partials) Solve the null equation for each variable
    and determine the coordinates of the critical point
	"""
	zero_x = Eq(partials[0], 0)
	zero_y = Eq(partials[1], 0)

	criticalpoint = solve((zero_x, zero_y), (symbols_list[0], symbols_list[1]))

	return criticalpoint

def determat(partials_second, cross_derivatives, criticalpoint, symbols_list):
    
	"""
	determat(partials_second, cross_derivatives, criticalpoint, symbols_list) gives a:
    List[sympy.core.add.Add] * sympy.core.add.Add * Dict[sympy.core.numbers.Float] * List[sympy.core.symbol.Symbol] 
    Hence we get a sympy.core.numbers.Float
	It computes the determinant of the Hessian matrix at the critical point.
	"""
	det = partials_second[0].subs([(symbols_list[0], criticalpoint[symbols_list[0]]), (symbols_list[1], criticalpoint[symbols_list[1]])]) * partials_second[1].subs([(symbols_list[0], criticalpoint[symbols_list[0]]), (symbols_list[1], criticalpoint[symbols_list[1]])]) - (cross_derivatives.subs([(symbols_list[0], criticalpoint[symbols_list[0]]), (symbols_list[1], criticalpoint[symbols_list[1]])]))**2

	return det

def eigenvalues(partials_second, cross_derivatives, criticalpoint, symbols_list):
    
    """
    The function scipy.linalg.eig computes eigenvalues and eigenvectors of a square matrix
    The function la.eig returns a tuple (eigvals,eigvecs)
    eigvals is a 1D NumPy array of complex numbers giving the eigenvalues of a matrix
    """
    l = symbols('l')
    lambdapartials = []
    for element in partials_second:
        partial_diff = element -l
        lambdapartials.append(partial_diff)

    zero = Eq(determat(lambdapartials, cross_derivatives, criticalpoint, symbols_list), 0)
    eigen = solve(zero, l)
    return eigen


def OptimaType(eigenvalues, det):
    firstelement = eigenvalues[0]
    if det != 0.0:
        for element in eigenvalues:
            if element > 0.0 and firstelement > 0.0:
                optima = "Minimum"
                
            if element < 0.0 and firstelement < 0.0:
                optima = "Maximum"
                
            if element*firstelement <0: 
                optima = "Saddle Point"
                
    else: optima = "no conclusion is possible"
    
    return optima 
def  Contour_Plot (symbols_list, function, criticalpoint):
    
    """ 
    We generate 50 values b/w 0 and 5 with np.linspace(0, 5, 50)
    And then we generate combination of grids with np.meshgrid
    Draw rectangular contour plot with plt.contour(X, Y, Z, cmap='black')
    """
    x1 = np.linspace((- 5.0), 5.0, 50,endpoint=True)
    y1 = np.linspace((- 5.0), 5.0, 50,endpoint=True)
    
    X, Y = np.meshgrid(x1, y1)
   
    f = lambdify(symbols_list, function, 'numpy')
    
    c = plt.contour(X, Y, f(X, Y))
    
    return c
        

def mainExercise1():
    """
	Main for exercise 1
	"""
    # Question 1 

    x, y = symbols('x y')
    symbols_list = [x, y]
    function = x**2 - (3/2)*x*y + y**2
    partials, partials_second = [], []
    
    for element in symbols_list:
    	partial_diff = partialderivative(element, function)
    	partials.append(partial_diff)
    
    grad = gradient(partials)
    
    print("The gradient vector of the function {0} is :\n {1}".format(function, grad))
    
    cross_derivatives = partialderivative(symbols_list[0], partials[1])
    
    for i in range(0, len(symbols_list)):
    		partial_diff = partialderivative(symbols_list[i], partials[i])
    		partials_second.append(partial_diff)
        
    hessianmatrix = hessian(partials_second, cross_derivatives)
    
    print("The Hessian matrix of the function {0} is :\n {1}".format(function, hessianmatrix))
    
    # end of question 1
    
    # Question 2
    
    criticalpoint = stationary_points(symbols_list, partials)
    
    det = determat(partials_second, cross_derivatives, criticalpoint, symbols_list)
    
    print("The determinant of the critical point {0} is :\n {1}".format(criticalpoint, det))
    # end of question 2
    
    # Question 3
    eigenvals = eigenvalues(partials_second, cross_derivatives, criticalpoint, symbols_list)
    optima = OptimaType(eigenvals, det)
    
    print("The eigenvalues are {0} of the matrix ".format(eigenvals))
    print("The stationary point: {0} is a :\n {1}".format(criticalpoint, optima))
    # end of question 3
    
    #Question 4
    x, y = symbols('x y')
    symbols_list = [x, y]
    print("The contour plot of the function:")
    Contour_Plot(symbols_list, function, criticalpoint)
    # end of question 4
    
mainExercise1()

    
