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
	
	public void insert(T elem) {
		elems.insert(elem);
		if(cmp.compare(elem, bestOne) < 0){
			bestOne = elem;
		}
	}
	public void remove(T elem) {
		if(cmp.compare(bestOne, elem) == 0){
			bestOne = elems.updateMax();
			return;
		}
		elems.remove(elem);
	}
}
