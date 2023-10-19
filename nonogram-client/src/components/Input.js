import Button from "./Button";

function Input(props){

    const onSetSize = ()=>{

        let size = document.getElementById("gamesize").value;

        if (size>15) size=15;
        else if(size<3) size=3;
        
        props.onSetSize(size)
    }

    return(
        <div className="Input">
            <label>Size: <input id="gamesize" type="number" max={15} min={3} defaultValue={5}/></label>
            <Button onClick={onSetSize} type={"submit"} name={"OK"}/>
        </div>
    );

}

export default Input;