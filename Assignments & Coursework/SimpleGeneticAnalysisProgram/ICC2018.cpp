// ICC2018.cpp : Ce fichier contient la fonction 'main'. L'exécution du programme commence et se termine à cet endroit.
//

#include <iostream>
#include <string>
#include <array>
using namespace std;

typedef char Base;
typedef string Sequence;
typedef array<Sequence, 4> Gene;

// Threshold from which the number of mutations becomes dangerous
constexpr double RATIO_TRESHOLD(0.15);

// size of the sequences
constexpr size_t SEQ_SIZE(10);

// Reference sequences of the K-RAS gene
const Gene KRAS = { "cgtatttaaa", "gagcccctgg", "gagtctccgg", "ctcaggcctg" };


bool isAT(Base x, Base y)
{
	return (x == 't' && y == 'a') || (x == 'a' && y == 't');
}

bool isCG(Base x, Base y)
{
	return (x == 'c' && y == 'g') || (x == 'g' && y == 'c');
}

bool isCT(Base x, Base y)
{
	return (x == 'c' && y == 't');
}

/*  
* returns the probability that the base x mutates becoming a y
 */
double probability(Base x, Base y)
{
	if (isAT(x, y)) { return 0.01; }

	if (isCG(x, y)) { return 0.02; }

	if (isCT(x, y)) { return 0.01; }

	return 0;
}

void analyze(double proba, double mutation_ratio);
void print(const Gene& gene);
Sequence compact(const Gene& gene);
size_t difference(const Sequence& seq, const Sequence& ref_seq);
double probability(const Sequence& seq, const Sequence& ref_seq);
void mutation(Gene& gene, size_t n);

/* PROVIDED TEST FUNCTIONS
 */
double test_proba(const Sequence& seq, const Sequence& ref_seq)
{
	cout << "Probability that the sequence to be analyzed comes"
		<< "from the reference sequence:";
	double proba(probability(seq, ref_seq));
	cout << proba << endl;
	return proba;
}

double test_compare(const Sequence& seq, const Sequence& ref_seq)
{
	cout << "The sequence to analyze";
	cout << "differs from the reference sequence in: " << endl;
	double diff(difference(seq, ref_seq));
	double ratio(diff / seq.size());
	cout << "altered bases ratio = " << ratio << endl;
	return ratio;
}

int main() {

	// TEST 1 
	cout << "Test 1 : " << endl;
	cout << "--------" << endl;

	analyze(0, 0.2);
	analyze(0.0001, 0.15);
	analyze(0.1, 0.00001);
	cout << endl;
	// END of TEST 1

	// TEST 2
	cout << "Test 2	: " << endl;
	cout << "--------" << endl;

	cout << "The reference portion of the K-RAS gene is:" << endl;
	print(KRAS);
	Sequence ref_seq(compact(KRAS));
	cout << "Compact representation of the reference portion:"
		<< endl
		<< ref_seq
		<< endl;
	cout << endl;

	cout << "Display of invalid genes: " << endl;

	Gene gene0 = { "cgta", "gagcccctcc", "gagtctccccttttt", "ttcaggcctg"
	};

	print(gene0);

	// END of TEST 2

	// TEST 3
	cout << "Test 3	: " << endl;
	cout << "--------" << endl;


	Gene gene1 = { "cgtatttaat", "gagcccctcc", "gagtctcccc", "ttcaggcctg"
	};

	cout << "Reference gene: ";
	print(KRAS);
	cout << " Gene to analyze: ";
	print(gene1);

	Sequence seq1(compact(gene1));

	double proba(0.0);
	proba = test_proba(seq1, ref_seq);
	cout << endl;
	// END of TEST 3

	// TEST 4
	cout << "Test 4 : " << endl;
	cout << "--------" << endl;


	double ratio(0.0);

	ratio = test_compare(seq1, ref_seq);
	analyze(proba, ratio);
	cout << endl;
	// End of TEST 4

	// TEST 5
	cout << "Test 5 : " << endl;
	cout << "--------" << endl;


	Gene gene2(KRAS);
	mutation(gene2, 3);

	cout << "Gene of reference : ";
	print(KRAS);
	cout << "Gene to analyze   : ";
	print(gene2);

	Sequence seq2(compact(gene2));
	proba = test_proba(seq2, ref_seq);

	ratio = test_compare(seq2, ref_seq);
	analyze(proba, ratio);
	// End of TEST 5	
	return 0;
}

void print(const Gene& gene)
{
	for (auto seq : gene) {
		cout << seq;

		if (seq.size() != SEQ_SIZE) {
			cout << '!';
		}
		cout << " ";
	}
	cout << endl;
}

Sequence compact(const Gene& gene)
{
	Sequence  sequence("");
	for (auto seq : gene) {
		sequence += seq;
	}
	return sequence;
}

double probability(const Sequence& sequence, const Sequence& reference)
{
	double prob(1.);
	for (size_t i(0); i < sequence.size(); ++i)
	{
		char base(sequence[i]);
		char ref_base(reference[i]);
		if (base != ref_base) {
			double local_prob(probability(ref_base, base));
			if (local_prob != 0)
				prob *= local_prob;
		}
	}
	return prob;
}

size_t difference(const Sequence& sequence, const Sequence& reference)
{
	size_t modified(0);
	for (size_t i(0); i < sequence.size(); ++i)
	{
		char base(sequence[i]);
		char ref_base(reference[i]);
		if (base != ref_base) {
			cout << base << " " << ref_base << endl;
			++modified;
		}
	}
	return modified;
}

void analyze(double proba, double mutation_ratio)
{
	cout << "Phenotype ";
	if (proba > 0 and mutation_ratio >= RATIO_TRESHOLD) {
		cout << "altered" << endl;
	}
	else {
		cout << "preserved" << endl;
	}
}

void mutation(Gene& gene, size_t n)
{
	size_t count(0);
	for (auto& seq : gene)
		for (auto& base : seq) {
			if (base == 'c' && count < n)
			{
				base = 't';
				++count;
				if (count >= n) return;
			}

		}
}
