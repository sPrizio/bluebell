export type SimpleImage = {
  src: any,
  alt: string,
  width?: number,
  height?: number,
}

export type ColumnHeroElement = {
  title: string,
  text: string,
}

export type StepSectionElement = {
  title: string,
  text: string,
  image: SimpleImage
}

export type ClientCardSectionElement = {
  title: string,
  text: string,
  image: {
    src: any,
    alt: string,
  }
}

export type StepImageSectionElement = {
  title: string,
  text: string,
  image: {
    src: any,
    alt: string,
  }
}