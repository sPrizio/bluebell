import React, {ChangeEvent, useState} from "react";
import styles from "./FileInput.module.scss";
import SimpleInput from "@/app/components/Input/SimpleInput";
import {BiImport} from "react-icons/bi";

/**
 * Custom input to handle file inputs
 *
 * @param val the file
 * @param handler what to do when a file is uploaded
 * @param displayValue what to display (typically the file name)
 * @author Stephen Prizio
 * @version 0.0.1
 */
function FileInput({val = null, handler, displayValue = ''}: Readonly<{ val: any, handler: Function, displayValue?: string }>) {

  const baseClass = "file-input"

  const [fileName, setFileName] = useState('Choose a file...')


  //  GENERAL FUNCTIONS

  /**
   * Handles the file upload change event
   * @param e
   */
  function handleFileUpload(e: ChangeEvent<HTMLInputElement>) {
    setFileName(e.target?.files?.[0]?.name ?? '')
    handler(e)
  }


  //  RENDER

  return (
    <div className={styles[baseClass]}>
      <label htmlFor="file-upload" className={styles[`${baseClass}__label`]}>
        <SimpleInput hasButton={true} buttonText={'Import'} isRounded={true} placeholder={'Choose a file...'}
                     isLoading={false} buttonIcon={<BiImport />} inputType={"file"}
                     val={displayValue}
        />
      </label>
      <input id="file-upload" type="file" onChange={handleFileUpload} />
    </div>
  )
}

export default FileInput;