General information

What you need to run these files
These files were developed and tested on Spyder scientific environement for python. 
The products need to run all of the files are any IDE for python 3.8

For both exercises you can change the "function" to any of these:
Matyas function
Himmelblau's function
McCormick function
or any Optimization Test Problems

Exercise 1:
partialderivative is basically sympy.core.symbol.Symbol * sympy.core.add.Add
    It gives the partial derivative of a function with several variables is its derivative,
    with respect to one of those variables while holding the rest constant 

gradient(partials) makes the List[sympy.core.add.Add] and hence a numpy.matrix
	So it transforms a list of sympy objects into a numpy matrix
	

hessian(partials_second, cross_derivatives) gives a List[sympy.core.add.Add] * sympy.core.add.Add 
    hence we get a numpy.matrix
	the same way as the gradient it transforms a list of sympy objects into a numpy hessian matrix
    I use the Schwarz’s theorem on the second partial derivatives of a function of several variables
	
       
stationary_points gives a List[sympy.core.symbol.Symbol] * List[sympy.core.add.Add] 
    hence we have a Dict[sympy.core.numbers.Float]
	stationary_points(symbols_list, partials) Solve the null equation for each variable
    and determine the coordinates of the critical point
	

determat(partials_second, cross_derivatives, criticalpoint, symbols_list) gives a:
    List[sympy.core.add.Add] * sympy.core.add.Add * Dict[sympy.core.numbers.Float] * List[sympy.core.symbol.Symbol] 
    Hence we get a sympy.core.numbers.Float
	It computes the determinant of the Hessian matrix at the critical point.

The function scipy.linalg.eig computes eigenvalues and eigenvectors of a square matrix
    The function la.eig returns a tuple (eigvals,eigvecs)
    eigvals is a 1D NumPy array of complex numbers giving the eigenvalues of a matrix
   
We generate 50 values with np.linspace(0, 5, 50)
    And then we generate combination of grids with np.meshgrid
    Draw rectangular contour plot with plt.contour(X, Y, Z, cmap='black')

Exercise 2:
Taylors Returns a Sympy expression of the second orger Taylor series,
    of a given multivariate expression, approximated as a multivariate polynomial evaluated at the given_point
    function: Sympy expression of the function
    symbols_list: list. All variables to be approximated (to be "Taylorized")
    given_point: list. Coordinates, where the function will be expressed
    
partialderivative is basically sympy.core.symbol.Symbol * sympy.core.add.Add
    It gives the partial derivative of a function with several variables is its derivative,
    with respect to one of those variables while holding the rest constant 
	
   
gradient(partials) makes the List[sympy.core.add.Add] and hence a numpy.matrix
	So it transforms a list of sympy objects into a numpy matrix
	
hessian(partials_second, cross_derivatives) gives a List[sympy.core.add.Add] * sympy.core.add.Add 
    hence we get a numpy.matrix
	the same way as the gradient it transforms a list of sympy objects into a numpy hessian matrix
    I use the Schwarz’s theorem on the second partial derivatives of a function of several variables
	
stationary_points gives a List[sympy.core.symbol.Symbol] * List[sympy.core.add.Add] 
    hence we have a Dict[sympy.core.numbers.Float]
	stationary_points(symbols_list, partials) Solve the null equation for each variable
    and determine the coordinates of the critical point
	
determat(partials_second, cross_derivatives, criticalpoint, symbols_list) gives a:
    List[sympy.core.add.Add] * sympy.core.add.Add * Dict[sympy.core.numbers.Float] * List[sympy.core.symbol.Symbol] 
    Hence we get a sympy.core.numbers.Float
	It computes the determinant of the Hessian matrix at the critical point.
	
Start of Newton's Method
Input initial guess (x0), tolerable error (e) 
   and maximum iteration (N) Initialize iteration counter i = 1
If det = 0 then print "Mathematical Error" and  If i >= N then print "Not Convergent" 
If |f(x1)| > e then set x0 = x1 and finally it prints root as x1

gradient_descent() is a basic implementation of the algorithm that starts with an arbitrary point, start, iteratively moves it toward the minimum, and returns a point that is hopefully at or near the minimum

Newton-Conjugate-Gradient algorithm (method='Newton-CG')
Newton-Conjugate Gradient algorithm is a modified Newton’s method and uses a conjugate gradient algorithm to (approximately) invert the local Hessian [NW]. Newton’s method is based on fitting the function locally to a quadratic form:
The scipy.optimize package provides several commonly used optimization algorithms. A detailed listing is available: scipy.optimize (can also be found by help(scipy.optimize)).


