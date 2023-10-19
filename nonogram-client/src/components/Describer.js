import MENU_OPTIONS from "../utils/menu";


function Describer(props){

    const click = ()=>{
        props.navigationChange(0);
    }

    let stateName = Object.keys(MENU_OPTIONS)[props.stateName];
    return(
        <div className="Describer">
            {props.stateName===0 && <h3>Options</h3>}
            {props.stateName!==0 && <button onClick={click}>Back</button>}
        </div>
    );

}

export default Describer;