# -*- coding: utf-8 -*-
"""
Created on Thu Oct 14 18:26:57 2021

@author: grivot
"""
#exercise 2
from sympy import factorial, Matrix, prod
from sympy import symbols, Eq, solve
import itertools
from numpy import linspace
from sympy.interactive.printing import init_printing
init_printing(use_unicode=False, wrap_line=False)
from sympy import lambdify
import matplotlib.pyplot as mpl
from sympy.plotting import plot3d
import numpy as np
from numpy import linalg as LA
from scipy.optimize import minimize



def Taylors(symbols_list,given_point, function):
    """
    Taylors Returns a Sympy expression of the second orger Taylor series,
    of a given multivariate expression, approximated as a multivariate polynomial evaluated at the given_point
    function: Sympy expression of the function
    symbols_list: list. All variables to be approximated (to be "Taylorized")
    given_point: list. Coordinates, where the function will be expressed
    """
    n_var = len(symbols_list)
    point_coordinates = [(i, j) for i, j in (zip(symbols_list, given_point))]  
    # list of tuples with variables and their given_point coordinates, to later perform substitution
    
    deriv_orders = list(itertools.product(range(2 + 1), repeat=n_var))  
    # list with exponentials of the partial derivatives
    deriv_orders = [deriv_orders[i] for i in range(len(deriv_orders)) if sum(deriv_orders[i]) <= 2]  
    # Discarding some higher-order terms
    n_terms = len(deriv_orders)
    deriv_orders_as_input = [list(sum(list(zip(symbols_list, deriv_orders[i])), ())) for i in range(n_terms)]  
    # Individual 2 of each partial derivative, of each term
    
    polynomial = 0
    for i in range(n_terms):
        partial_derivatives_at_point = function.diff(*deriv_orders_as_input[i]).subs(point_coordinates)  
        # e.g. df/(dx*dy**2)
        denominator = prod([factorial(j) for j in deriv_orders[i]])  
        # e.g. (1! * 2!)
        distances_powered = prod([(Matrix(symbols_list) - Matrix(given_point))[j] ** deriv_orders[i][j] for j in range(n_var)])  
        # e.g. (x-x0)*(y-y0)**2
        polynomial += partial_derivatives_at_point / denominator * distances_powered
    return polynomial

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

def newtonMethod(x0,e,N, symbols_list, grad, hessian, function,partials_second, cross_derivatives):
    print('\n\n*** NEWTON METHOD IMPLEMENTATION ***')
    lam = lambdify(symbols_list, function, modules=['numpy'])    
    step = 1
    flag = 1
    condition = True
    det = partials_second[0]* partials_second[1] - (cross_derivatives)**2
    
    while condition:
        if det == 0.0:
            print('Divide by zero error!')
            break
        hessian = Matrix(hessian)
        grad = Matrix(grad)
        x0 = Matrix(x0)
        A = hessian.inv()
        A = A.subs({symbols_list[0]: x0.T[0] , symbols_list[1]:x0.T[1]  })
        grad = grad.subs({symbols_list[0]: x0.T[0] , symbols_list[1]:x0.T[1]  })
        x1 = Matrix(x0 - np.matmul(A, grad).T)

        print(x1)
        
        print("Iteration {0}, x1 = {1} and f(x1) = {2}" .format(step, x1, lam(x1[0],x1[1])))
        x0 = x1
        step = step + 1
        
        if step > N:
            flag = 0
            break
        
        condition = abs(lam(x1[0], x1[1])) > e
    
    if flag==1:
            print("\nRequired root is: {0}".format(x1))
    else:
        print('\nNot Convergent.')
             
        
def gradient_descent(symbols_list, grad, start, learn_rate, n_iter, tolerance):
    start = Matrix(start)
    vector = start
    grad = Matrix(grad)
    grad = grad.subs({symbols_list[0]: start.T[0] , symbols_list[1]: start.T[1]})

    for _ in range(n_iter):
        diff = -learn_rate*grad
        print (diff)
        if np.all(np.abs(diff) <= tolerance):
            break
        vector += diff.T
    return vector

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





def steepest_descent(hessian, grad, start, symbols_list):
    """
    Solve Ax = b
    Parameter x: initial values
    """
    start = Matrix(start)
    x = start
    grad = Matrix(grad)
    grad = grad.subs({symbols_list[0]: start.T[0] , symbols_list[1]: start.T[1]})

    hessian = Matrix (hessian)
    hessian = hessian.subs({symbols_list[0]: start.T[0] , symbols_list[1]: start.T[1]})

    A = hessian
    
    b = A*x.T + grad
    r = b - A @ x.T
    
    while LA.norm(r) > 1e-10 :
        p = r
        q = A @ p
        alpha = (p @ r) / (p @ q)
        x = x + alpha * p
        r = r - alpha * q

    return x



def main():
    
    x = symbols('x')
    y = symbols('y')
    variable_list = [x,y]

    function = (x + 2*y - 7)^2 + (2*x + y - 5)^2

    #Question 1
    evaluation_point = [1,4]
    f2 = (Taylors(variable_list, evaluation_point, function))
    print("The second order Taylor series expansions, f2, of the function {0} is :\n {1}".format(function, f2))
    #end of question 1
    
    #Question 2 3D plotting
    
    plot3d(function, f2)
    
    #Question 2 2D plotting
    lam = lambdify(variable_list, function, modules=['numpy'])
    x_vals = linspace(0, 10, 100)
    y_vals = linspace(0, 10, 100)
    z_vals = lam(x_vals, y_vals)
    mpl.plot(z_vals)
    
    lam2 = lambdify(variable_list, f2, modules=['numpy'])
    x2_vals = linspace(0, 10, 100)
    y2_vals = linspace(0, 10, 100)
    z2_vals = lam2(x2_vals, y2_vals)
    mpl.plot(z2_vals)
    
    mpl.show()
    #End of Question 2
    
    #Question 3 for f
    
    partials, partials_second = [], []
    
    for element in variable_list:
    	partial_diff = partialderivative(element, function)
    	partials.append(partial_diff)
    
    grad = gradient(partials)
    
    print("The gradient vector of the function {0} is :\n {1}".format(function, grad))
    
    cross_derivatives = partialderivative(variable_list[0], partials[1])
    
    for i in range(0, len(variable_list)):
    		partial_diff = partialderivative(variable_list[i], partials[i])
    		partials_second.append(partial_diff)
        
    hessianmatrix = hessian(partials_second, cross_derivatives)
    
    print("The Hessian matrix of the function {0} is :\n {1}".format(function, hessianmatrix))
    
    #Question 3 for f2
    
    
    partialsf2, partials_secondf2 = [], []
    
    for element in variable_list:
    	partial_diff2 = partialderivative(element, f2)
    	partialsf2.append(partial_diff2)
    
    grad2 = gradient(partialsf2)
    
    print("The gradient vector of the f2: {0} is :\n {1}".format(f2, grad2))
    
    cross_derivatives2 = partialderivative(variable_list[0], partialsf2[1])
    
    for i in range(0, len(variable_list)):
    		partial_diff2 = partialderivative(variable_list[i], partialsf2[i])
    		partials_secondf2.append(partial_diff2)
        
    hessianmatrix2 = hessian(partials_secondf2, cross_derivatives2)
    
    print("The Hessian matrix of the f2: {0} is :\n {1}".format(f2, hessianmatrix2))
    
    #Question 4 for f
    criticalpoints_f = stationary_points(variable_list, partials)
    #Question 4 for f2
    criticalpoints_f2 = stationary_points(variable_list, partialsf2)
    
    print("The stationary pointS of: {0} are :\n {1}".format(function, criticalpoints_f))
    print("The stationary pointS of: {0} are :\n {1}".format(f2, criticalpoints_f2))
    
    #Question 5 Newton's method
        
    # Input Section
    x0 = input('Enter Guess: ')
    e = input('Tolerable Error: ')
    N = input('Maximum Step: ')
    learn_rate = input('Step size: ')

    
    # Converting x0 and e to float
    x0 = np.matrix(x0)
    e = float(e)
    learn_rate = float(learn_rate)
    
    # Converting N to integer
    N = int(N)
        
    # Starting Newton's Method for f2 and f
    newtonMethod(x0,e,N, variable_list, grad2, hessianmatrix2, f2, partials_secondf2, cross_derivatives2)

    #Starting The gradient (steepest) descent's Method for f2 and f
    # with given learning rate
    print (":\n The gradient (steepest) descent's Method with given learning rate we have: \n")

    vector = gradient_descent(variable_list, grad, x0, learn_rate, N, e)
    print("The minimum point of: {0} is :\n {1}".format(function, vector))
    vector2 = gradient_descent(variable_list,grad2, x0, learn_rate, N, e)
    print("The minimum point of: {0} is :\n {1}".format(f2, vector2))
    
    #wiTh max learning rate 
    print (":\n With max learning rate we have: \n")
    eigen = eigenvalues(partials_secondf2, cross_derivatives2, criticalpoints_f2, variable_list) 
    if eigen[0] > eigen[1]:
        Maxlearn_rate = 2/eigen[0]
    if eigen[0] < eigen[1]:
        Maxlearn_rate = 2/eigen[1]
    else: print("Max learn rate not possible")
    vectorMax = gradient_descent(variable_list, grad, x0, Maxlearn_rate, N, e)
    print("The minimum point of: {0} is :\n {1}".format(function, vectorMax))
    vector2Max = gradient_descent(variable_list,grad2, x0, Maxlearn_rate, N, e)
    print("The minimum point of: {0} is :\n {1}".format(f2, vector2Max))
    
    #with Learning Rate to Minimize Along the Line
    print (":\n With Learning Rate to Minimize Along the Line we have: \n")
    steepest_descent(hessianmatrix, grad, x0, variable_list)
    
    #with conjugate gradient 
    print (":\n With conjugate gradient we have: \n")
    x0 = np.array(x0)
    lam = lambdify(variable_list, function, modules=['numpy'])
    res = minimize(lam, x0, method='Newton-CG',
               jac=grad, hess=hessianmatrix,
               options={'xtol': 1e-8, 'disp': True})
    print(res.x)
    

main()
