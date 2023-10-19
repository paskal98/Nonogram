function Clue(props){

    const styleType = "Clue Clue__"+props.type;
    const styleTypeElement = "Clue__element Clue__element__"+props.type;

    const inputing = (data)=>{
        props.onChange(data);
    }

    if(props.gameField==="solver"){

        return(

            <div className={styleType}>
                {props.clue.map((element,index)=>{
                    return(
                        <input className={styleTypeElement} placeholder="." type="text" onChange={(e)=>inputing([props.indx,index,e.target.value,props.type])} />
                    );
                })}
            </div>
    
        );

    }

    return(

        <div className={styleType}>
            {props.clue.map((element)=>{
                return(
                    <div className={styleTypeElement}>
                        {element}
                    </div>
                );
            })}
        </div>

    );

}

export default Clue;