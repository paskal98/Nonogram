import NongramClues from "./NongramClues";
import NonogramGameField from "./NonogramGameField";

function Nonogram(props){


    return(
        <div className="Nonogram">

            <NongramClues onChange={props.onChange} gameField={props.gameField} type="row" size={props.size} clues={props.rowClues}/>

            <div className="Nonogram__grid">
                <NongramClues onChange={props.onChange} gameField={props.gameField} type="column" size={props.size} clues={props.columnClues}/>
                <NonogramGameField onFillCell={props.onFillCell}  size={props.size} grid={props.grid}/>
            </div>

        </div>
    );

}

export default Nonogram;