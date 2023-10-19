
import Cell from "./Cell";



function NonogramGameField(props){


    let gridRows = [];

        for (let i = 0,k=1; i < props.size; i++) {
            let gridRowsCells = [];
            
            for (let j = 0; j < props.size; j++) {
                if (props.grid.length > 0 && props.grid[i]) {
                    gridRowsCells.push( <Cell key={k} index={i+" "+j} onFillCell={props.onFillCell} filled={props.grid[i][j]}/> );
                }
                k++;
            }

            gridRows.push(
                <div key={i} className="NonogramGameField__row">
                {gridRowsCells}
                </div>
            );
        }


    return (

        <div className="NonogramGameField">
            
            {gridRows.map((row)=>{
                return row;
            })}
              
        </div>

    );

}

export default NonogramGameField;