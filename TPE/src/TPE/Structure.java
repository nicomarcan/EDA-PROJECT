package TPE;

import java.util.Comparator;

public class Structure<T> {
	private Comparator<T> cmp;
	private AVL<T> elems;
	private T bestOne;
	
	public Structure(Comparator<T> cmp) {
		super();
		this.cmp = cmp;
		elems = new AVL<T>(cmp);
	}
	
	public AVL<T> getElems() {
		return elems;
	}

	public void insert(T elem) {
		elems.insert(elem);
		//System.out.println(elem+" contra "+bestOne);
		if(bestOne == null ||cmp.compare(elem, bestOne) > 0){
			bestOne = elem;
		}
	}
	public T getBestOne() {
		return bestOne;
	}

	public void setBestOne(T bestOne) {
		this.bestOne = bestOne;
	}

	public void remove(T elem) {
		if(bestOne.equals(elem)){
			bestOne = elems.updateMax();
			return;
		}
		elems.remove(elem);
	}
}
