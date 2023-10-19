

function Cell(props){


    const fillCell=(e)=>{
        let indx =(e.target.getAttribute("index").split(" "));
        if(props.filled===false){
            indx.push(true);
        }
        else{
            indx.push(false);
        }
        props.onFillCell(indx);
    }


    let styleCell = "NonogramGameField__row__cell";
    if(props.filled===true)
        styleCell = "NonogramGameField__row__cell filled"

    return(

            <div
                onClick={fillCell}
                className={styleCell}
                index={props.index}
            ></div>

    );

}

export default Cell;