package jp.ac.anan_nct.pokereg.flick;

import jp.ac.anan_nct.pokereg.KeySetPattern;
import jp.ac.anan_nct.pokereg.MainActivity;
import jp.ac.anan_nct.pokereg.entity.Flick;
import jp.ac.anan_nct.pokereg.entity.FlickSet;
import jp.ac.anan_nct.pokereg.view.CartView;

public class FlickActions {
	
	FlickSet flicks;
	MainActivity main;
	CartView cart;
	
	public FlickActions(FlickSet flicks, MainActivity main, CartView cart){
		this.flicks = flicks;
		this.main = main;
		this.cart = cart;
	}
	
	public boolean execute(int[] indexes){
		Flick f = this.flicks.find(indexes);
		if(f == null) return false;
		int aid = f.getActionId();
		int arg = f.getArg();
		switch(FlickAction.getFlickAction(aid)){
		case NOP: break;
		case OPEN_CART: this.main.openCart(); break;
		case RESET_PRICE:
			this.cart.setPrice(0); break;
		case ADD_PRICE:
			this.cart.setPrice(this.cart.getPrice() + f.getArg()); break;
		case SUB_PRICE:
			this.cart.setPrice(this.cart.getPrice() - f.getArg()); break;
		case PUSH_PRICE:
			this.cart.pushNumber(f.getArg()); break;
		case POP_PRICE:
			this.cart.popNumber(); break;
		case SET_PRICE:
			this.cart.setPrice(f.getArg()); break;
		case ENTER:
			this.cart.enter(); break;
		case CLEAR:
			this.cart.clear(); break;
		case RESET_DISCOUNT:
			this.cart.setDiscount(0); break;
		case ADD_DISCOUNT:
			this.cart.setDiscount(this.cart.getDiscount() + f.getArg()); break;
		case SUB_DISCOUNT:
			this.cart.setDiscount(this.cart.getDiscount() - f.getArg()); break;
		case SET_DISCOUNT:
			this.cart.setDiscount(f.getArg()); break;
		case RESET_REDUCTION:
			this.cart.setReduction(0); break;
		case ADD_REDUCTION:
			this.cart.setReduction(this.cart.getReduction() + f.getArg()); break;
		case SUB_REDUCTION:
			this.cart.setReduction(this.cart.getReduction() - f.getArg()); break;
		case SET_REDUCTION:
			this.cart.setReduction(f.getArg()); break;
		case RESET_AMOUNT:
			this.cart.setAmount(1); break;
		case ADD_AMOUNT:
			this.cart.setAmount(this.cart.getAmount() + f.getArg()); break;
		case SUB_AMOUNT:
			this.cart.setAmount(this.cart.getAmount() - f.getArg()); break;
		case SET_AMOUNT:
			this.cart.setAmount(arg); break;
		case RESET_TOTAL:
			this.cart.resetAll(); break;
		case REVERT_CART:
			this.cart.cancel(); break;
		case RESET_CATEGORY:
			this.cart.resetCategory(); break;
		case NEXT_CATEGORY:
			this.cart.nextCategory(); break;
		case PREV_CATEGORY:
			this.cart.prevCategory(); break;
		case SET_CATEGORY:
			this.cart.setCategoryById(f.getArg()); break;
		case TOGGLE_INCLUDING_TAX:
			this.main.toggleIncludingTax(); break;
		case SET_INCLUDING_TAX:
			this.main.setIncludingTax(true); break;
		case SET_UNINCLUDING_TAX:
			this.main.setIncludingTax(false); break;
		case TOGGLE_USING_CAMERA:
			this.main.toggleUsingCamera(); break;
		case SET_USING_CAMERA:
			this.main.setUsingCamera(true); break;
		case SET_UNUSING_CAMERA:
			this.main.setUsingCamera(false); break;
		case TOGGLE_HAND:
			this.main.toggleHand(); break;
		case SET_LEFT_HAND:
			this.main.setLeftHand(); break;
		case SET_RIGHT_HAND:
			this.main.setRightHand(); break;
		case ECHO_MY_LUCK:
			this.main.echoMyLuck(); break;
		case TOGGLE_KEY_SET_PATTERN:
			this.main.toggleKeySetPattern();
		case SET_BOTTOMUP_KEY_SET_PATTERN:
			this.main.setKeySetPattern(KeySetPattern.BottomUp); break;
		case SET_TOPDOWN_KEY_SET_PATTERN:	
			this.main.setKeySetPattern(KeySetPattern.TopDown); break;
		case CAPTURE:
			this.main.capture(); break;
			
		}
		return true;
	}
	
}
